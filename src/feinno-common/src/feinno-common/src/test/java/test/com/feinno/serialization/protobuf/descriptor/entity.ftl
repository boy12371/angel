package ${package_name};

<#if has_list?? && has_list>
import java.util.List;
</#if>
<#if has_map?? && has_map>
import java.util.Map;
</#if>
import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;
<#if has_type?? && has_type>
import com.feinno.serialization.protobuf.ProtoType;
</#if>

public class ${name} extends ProtoEntity {

<#list item as subitem>
<#if subitem.required>
    @ProtoMember(value = ${subitem.field.number},<#if subitem.prototype??> type = ${subitem.prototype},</#if> required = true)
<#elseif subitem.prototype??>
    @ProtoMember(value = ${subitem.field.number}, type = ${subitem.prototype})
<#else>
    @ProtoMember(${subitem.field.number})
</#if>
    private ${subitem.typename} ${subitem.field.name};
    
</#list>

<#list item as subitem>
<#if subitem.bool>
    public ${subitem.typename} is${subitem.fieldname}() {
<#else>
	public ${subitem.typename} get${subitem.fieldname}() {
</#if>
        return ${subitem.field.name};
    }

    public void set${subitem.fieldname}(${subitem.typename} value) {
        this.${subitem.field.name} = value;
    }
    
</#list>
}

