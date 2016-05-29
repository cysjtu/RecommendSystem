# encoding=utf-8

import matplotlib.pyplot as plt;

import scipy.misc as misc;

import numpy as np;


import random


X=np.linspace(-np.pi,+np.pi,256,endpoint=True);

C,S=np.cos(X),np.sin(X)

plt.plot(X,C)

plt.plot(X,S)

plt.show()



