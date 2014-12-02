/**
 * Copyright (C) 2010-2014 Morgner UG (haftungsbeschränkt)
 *
 * This file is part of Structr <http://structr.org>.
 *
 * Structr is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Structr is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Structr.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.structr.schema.parser;

import org.apache.commons.lang3.StringUtils;
import org.structr.common.error.ErrorBuffer;
import org.structr.common.error.FrameworkException;
import org.structr.common.error.InvalidPropertySchemaToken;
import org.structr.core.entity.SchemaNode;
import org.structr.core.property.StringProperty;
import org.structr.schema.Schema;
import org.structr.schema.SchemaHelper.Type;

/**
 *
 * @author Christian Morgner
 */
public class StringPropertyParser extends PropertyParser {

	public StringPropertyParser(final ErrorBuffer errorBuffer, final String className, final String propertyName, final PropertyParameters params) {
		super(errorBuffer, className, propertyName, params);
	}

	@Override
	public String getPropertyType() {
		return StringProperty.class.getSimpleName();
	}

	@Override
	public String getValueType() {
		return String.class.getName();
	}

	@Override
	public String getUnqualifiedValueType() {
		return "String";
	}

	@Override
	public String getPropertyParameters() {
		return "";
	}

	@Override
	public Type getKey() {
		return Type.String;
	}

	@Override
	public void parseFormatString(final Schema entity, final String expression) throws FrameworkException {

		if ("[]".equals(expression)) {
			errorBuffer.add(SchemaNode.class.getSimpleName(), new InvalidPropertySchemaToken(expression, "invalid_validation_expression", "Empty validation expression."));
			return;
		}

		if ("multi-line".equals(expression)) {

			localValidator = ", \""  + expression + "\"";

		} else if (StringUtils.isNotBlank(expression)) {

			localValidator = ", new SimpleRegexValidator(\""  + expression + "\")";

		}
	}

	@Override
	public String getDefaultValueSource() {
		return "\"".concat(defaultValue).concat("\"");
	}
}
