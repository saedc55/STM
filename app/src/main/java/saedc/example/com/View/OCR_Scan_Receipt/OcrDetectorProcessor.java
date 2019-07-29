/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package saedc.example.com.View.OCR_Scan_Receipt;

import android.content.Context;
import android.util.SparseArray;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;

import saedc.example.com.View.OCR_Scan_Receipt.*;
import saedc.example.com.View.OCR_Scan_Receipt.GraphicOverlay;

/**
 * A very simple Processor which receives detected TextBlocks and adds them to the overlay
 * as OcrGraphics.
 */
public class OcrDetectorProcessor implements Detector.Processor<TextBlock> {

    Context context;
    private saedc.example.com.View.OCR_Scan_Receipt.GraphicOverlay<OcrGraphic> mGraphicOverlay;

    OcrDetectorProcessor(GraphicOverlay<OcrGraphic> ocrGraphicOverlay, Context context) {
        mGraphicOverlay = ocrGraphicOverlay;
        this.context = context;
    }

    /**
     * Called by the detector to deliver detection results.
     * If your application called for it, this could be a place to icon_save for
     * equivalent detections by tracking TextBlocks that are similar in location and content from
     * previous frames, or reduce noise by eliminating TextBlocks that have not persisted through
     * multiple detections.
     */
    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {
        mGraphicOverlay.clear();
        //final String result;
        //String detectedText = "";
        SparseArray<TextBlock> items = detections.getDetectedItems();
        for (int i = 0; i < items.size(); ++i) {

            final TextBlock item = items.valueAt(i);
            OcrGraphic graphic = new OcrGraphic(mGraphicOverlay, item);
            mGraphicOverlay.add(graphic);
            //detectedText += item.getValue();
        }

        /*result = detectedText;
        ((OcrCaptureActivity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    /**
     * Frees the resources associated with this detection processor.
     */
    @Override
    public void release() {
        mGraphicOverlay.clear();
    }
}
