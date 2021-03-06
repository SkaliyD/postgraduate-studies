package skalii.web.server.postgraduatestudies.entities


import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonView

import java.util.Date

import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.FetchType.LAZY
import javax.persistence.ForeignKey
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.SEQUENCE
import javax.persistence.Id
import javax.persistence.Index
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.OrderBy
import javax.persistence.SequenceGenerator
import javax.persistence.Table
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

import skalii.web.server.postgraduatestudies.entities.enums.*
import skalii.web.server.postgraduatestudies.repositories.DegreesRepository
import skalii.web.server.postgraduatestudies.repositories.DepartmentsRepository
import skalii.web.server.postgraduatestudies.repositories.SpecialitiesRepository
import skalii.web.server.postgraduatestudies.repositories.UsersRepository
import skalii.web.server.postgraduatestudies.views.View.*


@Entity(name = "User")
@JsonIgnoreProperties(value = ["hash", "salt"])
@SequenceGenerator(
        name = "users_seq",
        sequenceName = "users_id_user_seq",
        schema = "public",
        allocationSize = 1)
@Table(name = "users",
        schema = "public",
        indexes = [
            Index(name = "users_pkey",
                    columnList = "id_user",
                    unique = true),
            Index(name = "users_id_user_uindex",
                    columnList = "id_user",
                    unique = true),
            Index(name = "users_salt_uindex",
                    columnList = "salt",
                    unique = true),
            Index(name = "users_id_contact_info_uindex",
                    columnList = "id_contact_info",
                    unique = true),
            Index(name = "users_id_study_info_uindex",
                    columnList = "id_study_info",
                    unique = true),
            Index(name = "users_id_scientific_links_uindex",
                    columnList = "id_scientific_links",
                    unique = true)])
data class User(

        @Column(name = "id_user",
                nullable = false)
        @GeneratedValue(
                strategy = SEQUENCE,
                generator = "users_seq")
        @Id
        @get:JsonProperty(value = "id_user")
        @JsonView(REST::class)
        @NotNull
        val idUser: Int,

        @Column(name = "role",
                nullable = false)
        @Convert(converter = UserRole.Companion.EnumConverter::class)
        @get:JsonProperty(value = "role")
        @NotNull
        val role: UserRole = UserRole.EMPTY,

        @Column(name = "salt",
                length = 64,
                nullable = false)
        @get:JsonProperty(value = "salt")
        @JsonView(TREE::class)
        @NotNull
        @Size(max = 64)
        val salt: String = "",

        @Column(name = "hash",
                length = 64,
                nullable = false)
        @get:JsonProperty(value = "hash")
        @JsonView(TREE::class)
        @NotNull
        @Size(max = 64)
        val hash: String = "",

        @Column(name = "full_name_ua",
                length = 100,
                nullable = false)
        @get:JsonProperty(value = "full_name_ua")
        @JsonView(REST::class)
        @NotNull
        @Size(max = 100)
        val fullNameUa: String,

        @Column(name = "full_name_en",
                length = 100,
                nullable = false)
        @get:JsonProperty(value = "full_name_en")
        @JsonView(REST::class)
        @NotNull
        @Size(max = 100)
        val fullNameEn: String,

        @Column(name = "birthday",
                nullable = false)
        @get:JsonFormat(
                pattern = "yyyy-MM-dd",
                shape = JsonFormat.Shape.STRING,
                timezone = "Europe/Kiev")
        @get:JsonProperty(value = "birthday")
        @JsonView(REST::class)
        @NotNull
        val birthday: Date,

        @Column(name = "family_status")
        @Convert(converter = FamilyStatus.Companion.EnumConverter::class)
        @get:JsonProperty(value = "family_status")
        val familyStatus: FamilyStatus? = FamilyStatus.EMPTY,

        @Column(name = "children")
        @get:JsonProperty(value = "children")
        @JsonView(REST::class)
        val children: Int? = null,

        @Column(name = "academic_rank")
        @Convert(converter = AcademicRank.Companion.EnumConverter::class)
        @get:JsonProperty(value = "academic_rank")
        @JsonView(REST::class)
        val academicRank: AcademicRank? = AcademicRank.EMPTY

) {

    @JoinColumn(
            name = "id_degree",
            foreignKey = ForeignKey(name = "users_degrees_fkey"))
    @JsonIgnoreProperties(value = ["users"])
    @get:JsonProperty(value = "degree")
    @JsonView(REST::class)
    @ManyToOne(
            targetEntity = Degree::class,
            fetch = LAZY)
    var degree: Degree? = null

    @JoinColumn(
            name = "id_speciality",
            nullable = false,
            foreignKey = ForeignKey(name = "users_specialities_fkey"))
    @JsonIgnoreProperties(value = ["users"])
    @get:JsonProperty(value = "speciality")
    @JsonView(REST::class)
    @ManyToOne(
            targetEntity = Speciality::class,
            fetch = LAZY,
            optional = false)
    lateinit var speciality: Speciality

    @JoinColumn(
            name = "id_department",
            nullable = false,
            foreignKey = ForeignKey(name = "users_departments_fkey"))
    @JsonIgnoreProperties(value = ["users"])
    @get:JsonProperty(value = "department")
    @JsonView(REST::class)
    @ManyToOne(
            targetEntity = Department::class,
            fetch = LAZY,
            optional = false)
    lateinit var department: Department

    @JoinColumn(
            name = "id_contact_info",
            nullable = false,
            foreignKey = ForeignKey(name = "users_contact_info_fkey"))
    @JsonIgnoreProperties(value = ["user"])
    @get:JsonProperty(value = "contact_info")
    @JsonView(REST::class)
    @OneToOne(
            targetEntity = ContactInfo::class,
            fetch = LAZY,
            optional = false)
    lateinit var contactInfo: ContactInfo

    @JoinColumn(
            name = "id_scientific_links",
            nullable = false,
            foreignKey = ForeignKey(name = "users_scientific_links_fkey"))
    @JsonIgnoreProperties(value = ["user"])
    @get:JsonProperty(value = "scientific_links")
    @JsonView(REST::class)
    @OneToOne(
            targetEntity = ScientificLinks::class,
            fetch = LAZY,
            optional = false)
    lateinit var scientificLinks: ScientificLinks

    @JoinColumn(
            name = "id_study_info",
            foreignKey = ForeignKey(name = "users_study_info_fkey"))
    @JsonIgnoreProperties(value = ["user"])
    @get:JsonProperty(value = "study_info")
    @JsonView(STUDENT::class)
    @OneToOne(
            targetEntity = StudyInfo::class,
            fetch = LAZY)
    var studyInfo: StudyInfo? = null

    @JsonIgnoreProperties(value = ["instructor"])
    @get:JsonProperty(value = "students")
    @JsonView(INSTRUCTOR_TREE::class)
    @OneToMany(
            targetEntity = StudyInfo::class,
            mappedBy = "instructor")
    @OrderBy
    lateinit var students: MutableList<StudyInfo?>

    @JsonIgnoreProperties(value = ["user"])
    @get:JsonProperty(value = "sections")
    @JsonView(STUDENT_TREE::class)
    @OneToMany(
            targetEntity = Section::class,
            mappedBy = "user")
    @OrderBy
    lateinit var sections: MutableList<Section?>


    constructor() : this(
            0,
            UserRole.UNKNOWN,
            "",
            "",
            "Невідомий користувач",
            "Unknown User",
            Date(0),
            null,
            null,
            null
    )

    constructor(
            idUser: Int,
            role: UserRole,
            salt: String,
            hash: String,
            fullNameUa: String,
            fullNameEn: String,
            birthday: Date,
            familyStatus: FamilyStatus?,
            children: Int?,
            academicRank: AcademicRank?,
            degree: Degree?,
            speciality: Speciality,
            department: Department,
            contactInfo: ContactInfo,
            studyInfo: StudyInfo?,
            scientificLinks: ScientificLinks
    ) : this(
            idUser,
            role,
            salt,
            hash,
            fullNameUa,
            fullNameEn,
            birthday,
            familyStatus,
            children,
            academicRank
    ) {
        this.degree = degree
        this.speciality = speciality
        this.department = department
        this.contactInfo = contactInfo
        this.studyInfo = studyInfo
        this.scientificLinks = scientificLinks
    }

    override fun toString() =
            """User(
                idUser=$idUser,
                role=${role.value},
                fullNameUa=$fullNameUa,
                fullNameEn=$fullNameEn,
                birthday=$birthday,
                familyStatus=${familyStatus?.value},
                children=$children,
                academicRank=${academicRank?.value}
                )""".trimMargin()


    fun fixInitializedAdd(
            degreesRepository: DegreesRepository,
            specialitiesRepository: SpecialitiesRepository,
            departmentsRepository: DepartmentsRepository
    ): User {
        if (this.degree != null
                && this.degree!!.idDegree == 0
                && (this.degree!!.name != AcademicDegree.UNKNOWN
                        && this.degree!!.branch != BranchOfScience.UNKNOWN)) {
            this.degree = degreesRepository.get(
                    this.degree!!.name.value,
                    this.degree!!.branch.value
            )
        }
        if (this.speciality.idSpeciality == 0) {
            if (this.speciality.number != "Невiдомий код") {
                this.speciality = specialitiesRepository.get(
                        this.speciality.number
                )
            } else if (this.speciality.name != "Невідома спеціальність") {
                this.speciality = specialitiesRepository.get(
                        name = this.speciality.name
                )
            }
        }
        if (this.department.idDepartment == 0
                && this.department.name != "Невідома кафедра") {
            this.department = departmentsRepository.get(this.department.name)
        }
        return this
    }

    fun fixInitializedSet(
            usersRepository: UsersRepository,
            degreesRepository: DegreesRepository,
            specialitiesRepository: SpecialitiesRepository,
            departmentsRepository: DepartmentsRepository,
            email: String? = null,
            phoneNumber: String? = null
    ): User {
        var foundUser: User? = null
        if (!this::contactInfo.isInitialized) {
            foundUser = usersRepository.get(
                    try {
                        this.contactInfo.email
                    } catch (e: Exception) {
                        email
                    },
                    try {
                        this.contactInfo.phoneNumber
                    } catch (e: Exception) {
                        phoneNumber
                    }
            )
            this.contactInfo = foundUser.contactInfo
        }
        if (!this::speciality.isInitialized) {
            this.speciality = foundUser?.speciality
                    ?: usersRepository.get(
                    this.contactInfo.email,
                    this.contactInfo.phoneNumber
            ).speciality
        }
        if (!this::department.isInitialized) {
            this.department = foundUser?.department
                    ?: usersRepository.get(
                    this.contactInfo.email,
                    this.contactInfo.phoneNumber
            ).department
        }
        if (this.degree?.idDegree == null) {
            this.degree = foundUser?.degree
                    ?: usersRepository.get(
                    this.contactInfo.email,
                    this.contactInfo.phoneNumber
            ).degree
        }
        fixInitializedAdd(
                degreesRepository,
                specialitiesRepository,
                departmentsRepository
        )
        return this
    }

}