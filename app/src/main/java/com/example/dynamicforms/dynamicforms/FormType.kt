package com.example.dynamicforms

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class JsonData(
    @Json(name = "data") val data: FormData
)

@JsonClass(generateAdapter = true)
data class FormData(
    @Json(name = "formType") val formType: FormType
)

@JsonClass(generateAdapter = true)
data class FormType(
    @Json(name = "formTypeId") val formTypeId: String,
    @Json(name = "formIdentityFormIdentityId") val formIdentityFormIdentityId: String,
    @Json(name = "versionNumber") val versionNumber: Int,
    @Json(name = "publishedDate") val publishedDate: String,
    @Json(name = "draftFormTypeFormTypeId") val draftFormTypeFormTypeId: String,
    @Json(name = "name") val name: String,
    @Json(name = "rules") val rules: String,
    @Json(name = "ordinal") val ordinal: Int,
    @Json(name = "isDeleted") val isDeleted: Boolean,
    @Json(name = "rulesSummary") val rulesSummary: RulesSummary,
    @Json(name = "pages") val pages: List<FormPage>
)

@JsonClass(generateAdapter = true)
data class RulesSummary(
    @Json(name = "datasets") val datasets: List<String>
)

@JsonClass(generateAdapter = true)
data class FormPage(
    @Json(name = "pageId") val pageId: String,
    @Json(name = "draftPagePageId") val draftPagePageId: String,
    @Json(name = "name") val name: String,
    @Json(name = "ordinal") val ordinal: Int,
    @Json(name = "isVisible") val isVisible: Boolean,
    @Json(name = "sections") val sections: List<FormSection>
)

@JsonClass(generateAdapter = true)
data class FormSection(
    @Json(name = "pageSectionId") val pageSectionId: String,
    @Json(name = "draftPageSectionPageSectionId") val draftPageSectionPageSectionId: String,
    @Json(name = "name") val name: String?,
    @Json(name = "ordinal") val ordinal: Int,
    @Json(name = "hasHeader") val hasHeader: Boolean,
    @Json(name = "isVisible") val isVisible: Boolean,
    @Json(name = "parentSectionId") val parentSectionId: String?,
    @Json(name = "page") val page: PageInfo,
    @Json(name = "elements") val elements: List<FormElement>
)

@JsonClass(generateAdapter = true)
data class PageInfo(
    @Json(name = "pageId") val pageId: String,
    @Json(name = "isVisible") val isVisible: Boolean
)

@JsonClass(generateAdapter = true)
data class FormElement(
    @Json(name = "pageElementId") val pageElementId: String,
    @Json(name = "draftPageElementPageElementId") val draftPageElementPageElementId: String,
    @Json(name = "placeholderText") val placeholderText: String?,
    @Json(name = "elementType") val elementType: String,
    @Json(name = "isRequired") val isRequired: Boolean,
    @Json(name = "isVisible") val isVisible: Boolean,
    @Json(name = "isEditable") val isEditable: Boolean,
    @Json(name = "isDeleted") val isDeleted: Boolean,
    @Json(name = "name") val name: String,
    @Json(name = "ordinal") val ordinal: Int,
    @Json(name = "options") val options: List<Option>,
    @Json(name = "validations") val validations: List<Validation>
)

@JsonClass(generateAdapter = true)
data class Option(
    @Json(name = "pageElementOptionId") val pageElementOptionId: String,
    @Json(name = "ordinal") val ordinal: Int,
    @Json(name = "name") val name: String,
    @Json(name = "description") val description: String?,
    @Json(name = "isActive") val isActive: Boolean,
    @Json(name = "dataValue") val dataValue: String
)

@JsonClass(generateAdapter = true)
data class Validation(
    @Json(name = "regexValidator") val regexValidator: String,
    @Json(name = "regexValidatorFailureMessage") val regexValidatorFailureMessage: String,
    @Json(name = "ordinal") val ordinal: Int,
    @Json(name = "isActive") val isActive: Boolean
)
