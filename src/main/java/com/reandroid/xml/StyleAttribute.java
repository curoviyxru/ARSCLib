/*
 *  Copyright (C) 2022 github.com/REAndroid
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.reandroid.xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class StyleAttribute extends XMLAttribute {

    public StyleAttribute(){
        super();
    }

    @Override
    public StyleElement getParentNode() {
        return (StyleElement) super.getParentNode();
    }

    public void setFrom(XMLAttribute xmlAttribute) {
        set(xmlAttribute.getName(true),
                xmlAttribute.getValueAsString(false));
    }

    @Override
    public StyleAttribute set(String name, String value) {
        super.set(name, value);
        return this;
    }

    @Override
    public void serialize(XmlSerializer serializer) throws IOException {
        serializer.attribute(null, getName(), getValueAsString());
    }
}
