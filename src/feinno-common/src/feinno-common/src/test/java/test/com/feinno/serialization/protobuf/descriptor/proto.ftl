<#if package_name?? && package_name != "">
package ${package_name};
</#if>
<#if options?? && options.java_package?? && options.java_package != "">
option java_package = ${options.java_package};
</#if>

<#if enum_type??>
<#list enum_type as item>
enum ${item.name} {
  <#list item.value as subitem>
  ${subitem.name} = ${subitem.number};
  </#list>
}
</#list>
</#if>

<#if message_type??>
<#list message_type as item>
message ${item.name} {
  <#list item.field as subitem>
  ${subitem.label_name} ${subitem.proto_type_name}  ${subitem.name} = ${subitem.number};
  </#list>
}
</#list>
</#if>