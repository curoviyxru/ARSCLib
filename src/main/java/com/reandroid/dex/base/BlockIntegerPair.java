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
package com.reandroid.dex.base;

import com.reandroid.arsc.base.DirectStreamReader;
import com.reandroid.arsc.item.IndirectInteger;
import com.reandroid.arsc.item.BlockItem;
import com.reandroid.arsc.item.IntegerReference;

public class BlockIntegerPair extends BlockItem implements IntegerPair, DirectStreamReader {

    private final IntegerReference first;
    private final IntegerReference second;

    public BlockIntegerPair() {
        super(8);
        this.first = new IndirectInteger(this, 0);
        this.second = new IndirectInteger(this, 4);
    }
    @Override
    public IntegerReference getFirst() {
        return first;
    }
    @Override
    public IntegerReference getSecond() {
        return second;
    }
    @Override
    public String toString(){
        return "(" + getFirst() + ", " + getSecond() + ")";
    }
}
