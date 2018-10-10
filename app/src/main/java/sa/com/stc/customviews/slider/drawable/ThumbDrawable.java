/*
 * Copyright (c) Gustavo Claramunt (AnderWeb) 2014.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sa.com.stc.customviews.slider.drawable;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.os.SystemClock;
import android.support.annotation.NonNull;

public class ThumbDrawable extends StateDrawable implements Animatable {
    //The current size for this drawable. Must be converted to real DPs
    public static final int DEFAULT_SIZE_DP = 12;
    private final int mSize;
    private boolean mOpen;
    private boolean mRunning;
    private Runnable opener = new Runnable() {
        @Override
        public void run() {
            mOpen = true;
            invalidateSelf();
            mRunning = false;
        }
    };

    public ThumbDrawable(@NonNull ColorStateList tintStateList, int size) {
        super(tintStateList);
        mSize = size;
    }

    @Override
    public int getIntrinsicWidth() {
        return mSize;
    }

    @Override
    public int getIntrinsicHeight() {
        return mSize;
    }

    @Override
    public void doDraw(Canvas canvas, Paint paint) {
        if (!mOpen) {
            Rect bounds = getBounds();
            float radius = (mSize / 2);
            canvas.drawCircle(bounds.centerX(), bounds.centerY(), radius, paint);
        }
    }

    public void animateToPressed() {
        scheduleSelf(opener, SystemClock.uptimeMillis() + 100);
        mRunning = true;
    }

    public void animateToNormal() {
        mOpen = false;
        mRunning = false;
        unscheduleSelf(opener);
        invalidateSelf();
    }

    @Override
    public void start() {
        //NOOP
    }

    @Override
    public void stop() {
        animateToNormal();
    }

    @Override
    public boolean isRunning() {
        return mRunning;
    }
}