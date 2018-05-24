package skaliy.web.server.postgraduatestudies.entities


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonView

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Index
import javax.persistence.OneToMany
import javax.persistence.OrderBy
import javax.persistence.SequenceGenerator
import javax.persistence.Table
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

import skaliy.web.server.postgraduatestudies.views.View


@Entity(name = "Institute")
@SequenceGenerator(
        name = "institutes_seq",
        sequenceName = "institutes_id_institute_seq",
        schema = "public",
        allocationSize = 1)
@Table(name = "institutes",
        schema = "public",
        indexes = [
            Index(name = "institutes_pkey",
                    columnList = "id_institute",
                    unique = true),
            Index(name = "institutes_id_institute_uindex",
                    columnList = "id_institute",
                    unique = true),
            Index(name = "institutes_name_uindex",
                    columnList = "name",
                    unique = true)])
data class Institute(

        @Column(name = "id_institute",
                nullable = false)
        @GeneratedValue(
                strategy = GenerationType.SEQUENCE,
                generator = "institutes_seq")
        @Id
        @JsonProperty(value = "id_institute")
        @JsonView(View.REST::class)
        @NotNull
        val idInstitute: Int,

        @NotNull
        @Size(max = 200)
        @Column(name = "name",
                nullable = false,
                length = 200)
        @JsonProperty(value = "name")
        @JsonView(View.UI::class)
        val name: String,

        @Column(name = "named_after",
                length = 100)
        @JsonProperty(value = "named_after")
        @JsonView(View.UI::class)
        @Size(max = 100)
        val namedAfter: String?,

        @Column(name = "abbreviation",
                length = 25)
        @JsonProperty(value = "abbreviation")
        @JsonView(View.UI::class)
        @Size(max = 25)
        val abbreviation: String?

) {

    @JsonIgnoreProperties(value = ["institute"])
    @JsonProperty(value = "departments")
    @JsonView(View.TREE::class)
    @OneToMany(
            targetEntity = Department::class,
            mappedBy = "institute")
    @OrderBy
    lateinit var departments: MutableList<Department>


    constructor() : this(
            0,
            "",
            "",
            "")

}