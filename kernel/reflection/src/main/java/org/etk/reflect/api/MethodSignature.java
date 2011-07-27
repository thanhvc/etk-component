package org.etk.reflect.api;

import java.util.Arrays;
import java.util.List;
/**
 * Method signature define for MethodInfo.
 * 
 * @author thanh_vucong
 *
 */
public class MethodSignature {
	private static final String[] NO_PARAMETERS = new String[0];

	private final String name;

	private final String[] parameterRawNames;

	/**
	 * 
	 * 
	 * @param name
	 * @param parameterTypes
	 */
	public MethodSignature(String name, Class<?>... parameterTypes) {
		if (name == null) {
			throw new NullPointerException("No null name accepted");
		}
		if (parameterTypes == null) {
			throw new NullPointerException("No null name accepted");
		}

		String[] parameterRawNames;

		if (parameterTypes.length == 0) {
			parameterRawNames = NO_PARAMETERS;
		} else {
			parameterRawNames = new String[parameterTypes.length];
			for (int i = 0; i < parameterTypes.length; i++) {
				Class<?> parameterType = parameterTypes[i];
				if (parameterType == null) {
					throw new IllegalArgumentException("Not null parameter type allowed");
				}

				parameterRawNames[i] = parameterType.getName();
			}
		}
		this.name = name;
		this.parameterRawNames = parameterRawNames;
	}

	public MethodSignature(String name, List<TypeInfo> parameterTypes) {
		if (name == null) {
			throw new NullPointerException("No null name accepted");
		}
		if (parameterTypes == null) {
			throw new NullPointerException("No null name accepted");
		}

		//
		String[] parameterRawNames;
		if (parameterTypes.size() == 0) {
			parameterRawNames = NO_PARAMETERS;
		} else {
			parameterRawNames = new String[parameterTypes.size()];
			int j = 0;
			for (TypeInfo parameterType : parameterTypes) {
				if (parameterType == null) {
					throw new IllegalArgumentException(
							"No null parameter type allowed");
				}
				parameterRawNames[j++] = computeRawName(parameterType);
			}
		}

		//
		this.name = name;
		this.parameterRawNames = parameterRawNames;
	}

	public String getName() {
		return name;
	}

	private String computeRawName(TypeInfo type) {
		if (type instanceof ClassTypeInfo) {
			return ((ClassTypeInfo) type).getName();
		} else if (type instanceof ParameterizedTypeInfo) {
			return computeRawName(((ParameterizedTypeInfo) type).getRawType());
		} else if (type instanceof TypeVariableInfo) {
			return computeRawName(((TypeVariableInfo) type).getBounds().get(0));
		} else if (type instanceof ArrayTypeInfo) {
			return "[L"
					+ computeRawName(((ArrayTypeInfo) type).getComponentType())
					+ ";";
		} else {
			throw new AssertionError();
		}
	}

	@Override
	public int hashCode() {
		return name.hashCode() ^ Arrays.hashCode(parameterRawNames);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof MethodSignature) {
			MethodSignature that = (MethodSignature) obj;
			return name.equals(that.name)
					&& Arrays.equals(parameterRawNames, that.parameterRawNames);
		}
		return false;
	}

	@Override
	public String toString() {
		return "MethodSignature[name=" + name + ",parameterRawNames="	+ Arrays.toString(parameterRawNames) + "]";
	}

}
