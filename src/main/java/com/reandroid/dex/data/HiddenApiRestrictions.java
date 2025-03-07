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
package com.reandroid.dex.data;

import com.reandroid.arsc.io.BlockReader;
import com.reandroid.arsc.item.IntegerItem;
import com.reandroid.dex.base.UsageMarker;
import com.reandroid.dex.id.ClassId;
import com.reandroid.dex.key.Key;
import com.reandroid.dex.sections.Section;
import com.reandroid.dex.sections.SectionType;

import java.io.IOException;

public class HiddenApiRestrictions extends DataItem{

    private final IntegerItem sizeReference;
    private final HiddenApiIndexList hiddenApiIndexList;
    private final HiddenApiDataList hiddenApiDataList;

    public HiddenApiRestrictions() {
        super(3);
        this.sizeReference = new IntegerItem();
        this.hiddenApiIndexList = new HiddenApiIndexList();
        this.hiddenApiDataList = new HiddenApiDataList();
        addChildBlock(0, sizeReference);
        addChildBlock(1, hiddenApiIndexList);
        addChildBlock(2, hiddenApiDataList);
    }

    public HiddenApiFlagValue getFlagValue(Key key) {
        return getHiddenApiIndexList().getFlagValue(key);
    }
    public HiddenApiIndexList getHiddenApiIndexList() {
        return hiddenApiIndexList;
    }
    public HiddenApiDataList getHiddenApiDataList() {
        return hiddenApiDataList;
    }
    HiddenApiIndex createNew(ClassId classId) {
        return getHiddenApiIndexList().createNext(classId,
                getHiddenApiDataList().createNext());
    }
    public boolean isAllNoRestrictions() {
        return getHiddenApiIndexList().isAllNoRestrictions();
    }
    public void removeIfAllNoRestrictions() {
        Section<HiddenApiRestrictions> section = getSection(SectionType.HIDDEN_API);
        if(section == null) {
            return;
        }
        if (!isAllNoRestrictions()) {
            return;
        }
        getHiddenApiIndexList().clearChildes();
        HiddenApiDataList hiddenApiDataList = getHiddenApiDataList();
        hiddenApiDataList.setItem(null);
        hiddenApiDataList.setParent(null);
        this.removeSelf();
        section.removeSelf();
    }

    @Override
    protected void onRefreshed() {
        super.onRefreshed();
        updateSizeFast();
        updateUsageType();
    }

    private void updateUsageType() {
        if (getIndex() != 0) {
            return;
        }
        if (getUsageType() != UsageMarker.USAGE_NONE) {
            return;
        }
        Section<ClassId> section = getSection(SectionType.CLASS_ID);
        if (section != null && !section.isEmpty()) {
            addUsageType(UsageMarker.USAGE_DEFINITION);
        }
    }

    private void updateSizeFast(){
        int size = 0;
        HiddenApiData last = hiddenApiDataList.getLast();
        if(last != null){
            size = last.getOffset();
            if(size != 0){
                size += last.countBytes();
            }
        }
        if(size == 0 || size != sizeReference.get()){
            size = countBytes();
        }
        sizeReference.set(size);
    }
    int getHiddenApiDataListOffset(){
        return sizeReference.countBytes() + hiddenApiIndexList.countBytes();
    }

    @Override
    public void onReadBytes(BlockReader reader) throws IOException {
        sizeReference.readBytes(reader);
        reader.offset(-sizeReference.countBytes());
        int size = sizeReference.get();
        BlockReader restrictionsReader = reader.create(size);
        restrictionsReader.offset(sizeReference.countBytes());
        hiddenApiIndexList.onReadBytes(restrictionsReader);
        hiddenApiDataList.onReadBytes(restrictionsReader);
        restrictionsReader.close();
        reader.seek(size);
    }
    @Override
    public SectionType<HiddenApiRestrictions> getSectionType() {
        return SectionType.HIDDEN_API;
    }

    @Override
    public String toString() {
        return "index = " + this.hiddenApiIndexList +
                ", data = " + this.hiddenApiDataList;
    }
}
