from flask import Flask, render_template, jsonify
import os
import numpy as np
import pandas as pd
import tensorflow as tf
import cv2 as cv
# from keras.preprocessing.image import ImageDataGenerator, img_to_array
from tensorflow.keras.models import Sequential, load_model
from facenet_pytorch import MTCNN
import time
from sklearn.metrics import log_loss
from tensorflow.keras import optimizers
from tensorflow.keras.optimizers import Adam

app = Flask(__name__)

# model = load_model('deepfake-detection2.h5')
model = load_model("Xcept-55-0.0002.h5")
# model.compile(loss='binary_crossentropy', optimizer="adam", metrics=['acc'])

@app.route('/predict')
def predict():
    print('kk')
    detector = MTCNN(margin=50, keep_all=False, post_process=False,thresholds=[.9,.9,.9]) # , device='cuda:0'
    predict_all=[]
    count=0
    v_cap = cv.VideoCapture('/static/test1.mp4')
    v_len = int(v_cap.get(cv.CAP_PROP_FRAME_COUNT))
    print(v_len)
    for frm in range(v_len):    
        success = v_cap.grab()
        if frm > 15:
            break
        print(frm)

        if frm % 1 == 0:
            success, frame = v_cap.retrieve()
            if not success:
                continue
            frame = cv.cvtColor(frame, cv.COLOR_BGR2RGB)
            frame = detector(frame)
            if frame is not None:
                frame = np.transpose(frame, (1, 2, 0))
                frame = np.array(cv.resize(np.array(frame),(160 ,160)))
                frame = (frame.flatten() / 255.0).reshape(-1, 160, 160, 3)
                count=count+frame.shape[0]
                predict = model.predict(frame)
                predict_all.append(predict)
            else:
                continue
        else:
            continue


    if (count>11):
        predict_all.sort()
        final_pred = sum(predict_all[5:-5])/(count-10)
        if final_pred==1: final_pred=[[0.9999]]
        elif final_pred==0: final_pred=[[0.0001]]
    else:
        final_pred=[[0.5]]   
    v_cap.release()


    print(predict_all)

    acc = (int(final_pred[0][0]*10000))/100

    print(acc)

    return jsonify(acc=acc)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)