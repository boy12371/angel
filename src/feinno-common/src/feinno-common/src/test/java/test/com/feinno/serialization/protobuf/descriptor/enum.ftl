package ${package_name};

import com.feinno.util.EnumInteger;

public enum ${item.name} implements EnumInteger {
<#list item.value as subitem>
    ${subitem.name}(${subitem.number}),
</#list>
    ;

    private int value = 0;
  
    ${item.name}(int value) {
        this.value = value;
    }
  
    @Override
    public int intValue() {
      return value;
    }
}
