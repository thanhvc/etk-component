package org.etk.model.apt;

import java.util.List;

import org.etk.model.apt.FormatterStyle;
import org.etk.reflect.api.ArrayTypeInfo;
import org.etk.reflect.api.ClassTypeInfo;
import org.etk.reflect.api.ParameterizedTypeInfo;
import org.etk.reflect.api.SimpleTypeInfo;
import org.etk.reflect.api.TypeInfo;
import org.etk.reflect.api.TypeVariableInfo;
import org.etk.reflect.api.VoidTypeInfo;
import org.etk.reflect.api.WildcardTypeInfo;

public class TypeFormatter {

  /** . */
  private final ClassTypeInfo context;

  /** . */
  private final FormatterStyle style;

  /** . */
  private final StringBuilder s;

  public TypeFormatter(ClassTypeInfo context, FormatterStyle style, StringBuilder s) {
    this.context = context;
    this.style = style;
    this.s = s;
  }

  public void format(TypeInfo ti) {
    format(ti, false);
  }

  private void format(TypeInfo ti, boolean fromArray) {
    if (ti instanceof ClassTypeInfo) {
      format((ClassTypeInfo)ti, fromArray);
    } else if (ti instanceof ParameterizedTypeInfo) {
      format((ParameterizedTypeInfo)ti, fromArray);
    } else if (ti instanceof TypeVariableInfo) {
      format((TypeVariableInfo)ti, fromArray);
    } else if (ti instanceof ArrayTypeInfo) {
      format((ArrayTypeInfo)ti, fromArray);
    } else if (ti instanceof WildcardTypeInfo) {
      format((WildcardTypeInfo)ti, fromArray);
    } else {
      throw new UnsupportedOperationException();
    }
  }

  private void format(ClassTypeInfo cti, boolean fromArray) {
    if (cti instanceof VoidTypeInfo) {
      switch (style) {
        case CAST:
        case TYPE_PARAMETER:
          throw new AssertionError();
        case RETURN_TYPE:
        case LITERAL:
          s.append("void");
          break;
      }
    }
    else {
      switch (style) {
        case CAST:
          if (!fromArray) {
            if (cti instanceof SimpleTypeInfo) {
              switch (((SimpleTypeInfo)cti).getLiteralType()) {
                case INT:
                  s.append("java.lang.Integer");
                  break;
                case BOOLEAN:
                  s.append("java.lang.Boolean");
                  break;
                case LONG:
                  s.append("java.lang.Long");
                  break;
                case DOUBLE:
                  s.append("java.lang.Double");
                  break;
                case FLOAT:
                  s.append("java.lang.Float");
                  break;
                default:
                  throw new UnsupportedOperationException();
              }
            } else {
              formatCompileTimeName(cti);
            }
          } else {
            formatCompileTimeName(cti);
          }
          break;
        case LITERAL:
        case TYPE_PARAMETER:
        case RETURN_TYPE:
          formatCompileTimeName(cti);
          break;
      }
    }
  }

  private void formatCompileTimeName(ClassTypeInfo cti) {
    ClassTypeInfo enclosing = cti.getEnclosing();
    if (enclosing != null) {
      formatCompileTimeName(enclosing);
      s.append('.');
      String classElementName = cti.getSimpleName().substring(enclosing.getSimpleName().length() + 1);
      s.append(classElementName);
    } else {
      s.append(cti.getName());
    }
  }

  private void format(ParameterizedTypeInfo pti, boolean fromArray) {
    TypeInfo rawType = pti.getRawType();
    format(rawType);
  }

  private void format(TypeVariableInfo tvi, boolean fromArray) {
    switch (style) {
      case LITERAL:
        format(tvi.getBounds().get(0));
        break;
      case TYPE_PARAMETER:
      case RETURN_TYPE:
      case CAST: {
        TypeInfo resolved = context.resolve(tvi);
        if (resolved instanceof TypeVariableInfo) {
          TypeVariableInfo resolvedTVI = (TypeVariableInfo)resolved;
          List<TypeInfo> bounds = resolvedTVI.getBounds();
          if (bounds.size() != 1) {
            throw new UnsupportedOperationException("Need to add support for multiple bounds");
          }
          TypeInfo bound = bounds.get(0);
          format(bound);
        } else {
          format(resolved);
        }
        break;
      }
    }
  }

  private void format(ArrayTypeInfo ati, boolean fromArray) {
    switch (style) {
      case LITERAL:
      case TYPE_PARAMETER:
      case RETURN_TYPE:
      case CAST: {
        TypeInfo componentTI = ati.getComponentType();
        format(componentTI, true);
        s.append("[]");
        break;
      }
    }
  }

  private void format(WildcardTypeInfo wti, boolean fromArray) {
    // Do nothing
  }
}
