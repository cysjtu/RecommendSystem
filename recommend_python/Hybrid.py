# encoding=utf-8

#from scikits.learn import linear_model as lm
import scikits.learn.linear_model as lm
import math
import scikits.learn.svm as svm
#import scikits.learn.ball_tree as tree

import sklearn.naive_bayes as nb


import sklearn.tree as tree

import random



def readdata(path,cols=[0,1,3]):
    f=open(path,"r")
    lines=f.readlines()
    ret={}

    for i in range(1,len(lines)):
        line=lines[i]
        ss=line.split(" ")
            #print ss
        user=int(ss[cols[0]])
        item=int(ss[cols[1]])
        rate=float(ss[cols[2]])
        ret[user,item]=rate;


    return ret


def Hybrid( **algo):


    testset=algo["test"]
    del algo["test"]

    print algo.keys()



    X=[]
    Y=[]
    tmp={}
    for k in algo.keys():
        v=algo[k]
        for ui in v.keys():
            r=v[ui]
            if not tmp.has_key(ui):
                tmp[ui]=[]

            tmp[ui].append(r)



    for ui in tmp.keys():
        X.append(tmp[ui])
        Y.append(testset[ui])

    print "begin fit===\n"
    DT_reg(algo=algo,tmp=tmp,testset=testset,X=X,Y=Y) ;print "\n"
    #svm_reg(algo=algo, tmp=tmp, testset=testset, X=X, Y=Y)
    linear_reg(algo=algo, tmp=tmp, testset=testset, X=X, Y=Y);print "\n"
    naiveBayse(algo=algo, tmp=tmp, testset=testset, X=X, Y=Y);print "\n"


def svm_reg(algo={},tmp={},testset={},X=None,Y=None):

    print "svm_reg"
    svr_poly=svm.SVR(kernel="linear")

    pred=svr_poly.fit(X,Y).predict(X)


    RMSE2(pred=pred,test=Y);

def getSample(X=None,Y=None,ratio=0.7):
    sample = random.sample(range(len(Y)), int(len(Y) * ratio))
    sms = {}
    for i in sample:
        sms[i] = 1

    Xtrain = []
    Xtest = []
    Ytrain = []
    Ytest = []
    for i in range(len(Y)):
        if sms.has_key(i):
            Xtrain.append(X[i])
            Ytrain.append(Y[i])
        else:
            Xtest.append(X[i])
            Ytest.append(Y[i])

    return Xtrain,Ytrain,Xtest,Ytest

def DT_reg(algo={},tmp={},testset={},X=None,Y=None):
    print "desition tree"

    Xtrain, Ytrain, Xtest, Ytest = getSample(X=X, Y=Y, ratio=0.8)

    reg=tree.DecisionTreeRegressor()
    reg2=tree.DecisionTreeClassifier()

    print "DecisionTreeRegressor"
    pred=reg.fit(Xtrain,Ytrain).predict(Xtest)
    RMSE2(pred=pred, test=Ytest)
    MAE(pred=pred, test=Ytest)
    ClassifyError(pred=pred, test=Ytest)


    print "DecisionTreeClassifier"
    pred=reg2.fit(Xtrain,Ytrain).predict(Xtest)
    RMSE2(pred=pred, test=Ytest)
    MAE(pred=pred, test=Ytest)
    ClassifyError(pred=pred, test=Ytest)



def naiveBayse(algo={},tmp={},testset={},X=None,Y=None):
    print "naiveBayse"
    Xtrain, Ytrain, Xtest, Ytest = getSample(X=X, Y=Y, ratio=0.8)

    bayse1=nb.GaussianNB()
    bayse2=nb.BernoulliNB()
    bayse3=nb.MultinomialNB()

    print "GaussianNB"
    pred=bayse1.fit(Xtrain,Ytrain).predict(Xtest)
    RMSE2(pred=pred, test=Ytest)
    MAE(pred=pred, test=Ytest)
    ClassifyError(pred=pred, test=Ytest)

    print "BernoulliNB"
    pred=bayse2.fit(Xtrain,Ytrain).predict(Xtest)
    RMSE2(pred=pred, test=Ytest)
    MAE(pred=pred, test=Ytest)
    ClassifyError(pred=pred, test=Ytest)

    print "MultinomialNB"
    pred=bayse3.fit(Xtrain,Ytrain).predict(Xtest)
    RMSE2(pred=pred, test=Ytest)
    MAE(pred=pred, test=Ytest)
    ClassifyError(pred=pred, test=Ytest)



def linear_reg(algo={},tmp={},testset={},X=None,Y=None):
    Xtrain, Ytrain, Xtest, Ytest = getSample(X=X, Y=Y,ratio=0.6)
    #lm.LinearRegression
    print "linear_reg"
    reg = lm.Ridge(alpha=0.25)
    #reg=lm.Lasso(alpha=0.0005)
    pred=reg.fit(Xtrain, Ytrain).predict(Xtest)


    #print algo.keys()
    #print reg.coef_
    #print reg.intercept_



    RMSE2(pred=pred, test=Ytest)
    MAE(pred=pred, test=Ytest)
    ClassifyError(pred=pred, test=Ytest)

    print "LogisticRegression"
    logreg=lm.LogisticRegression(penalty="l2",C=2);
    pred=logreg.fit(Xtrain,Ytrain).predict(Xtest)
    RMSE2(pred=pred, test=Ytest)
    MAE(pred=pred, test=Ytest)
    ClassifyError(pred=pred, test=Ytest)


#test : rank file

def genRankTest(train="",test="",toTest="",toTestLines="",cols=[0,1,2]):
    random.seed=1

    ftrain = open(train, "r")
    lines_train = ftrain.readlines()
    items={} ;users={}
    print "read %s"%(train)
    for i in range(1, len(lines_train)):
        line = lines_train[i]
        ss = line.split(" ")
        # print ss
        user = int(ss[cols[0]])
        item = int(ss[cols[1]])
        items[item]=1
        users[user]=1



    ret = {}
    ftest=open(test,"r")
    lines_test=ftest.readlines()


    print "read %s"%(test)
    for i in range(1, len(lines_test)):
        line = lines_test[i]
        ss = line.split(" ")
        # print ss
        user = int(ss[cols[0]])
        item = int(ss[cols[1]])
        rate = float(ss[cols[2]])
        if rate<9.0 :
            continue

        ret[user, item] = rate;



    print "begin generate"
    itemids=items.keys()

    preds={}
    preds_line={}
    SIZE=100

    for u,i in ret.keys():
        preds[u,i]=1


        rand_items=random.sample(itemids,SIZE)
        for it in rand_items:
            preds[u,it]=1

        preds_line[u,i]=rand_items


    ftoTest=open(toTest,"w")
    ftoTest.write("user item rate\n")

    for u,i in preds.keys():
        ftoTest.write(str(u)+" "+str(i)+" "+"0.0\n")

    ftoTest.close()


    ftoTestLines=open(toTestLines,"w")
    ftoTestLines.write("user item rate\n")

    for u,i in preds_line.keys():
        ftoTestLines.write(str(u)+" "+str(i));
        for k in preds_line[u,i]:
            ftoTestLines.write(" "+str(k))
        ftoTestLines.write("\n")


    ftoTestLines.close()



#======================


def RMSE(pred={},test={}):
    rmse=0.0
    cnt=0.0001
    for k in pred.keys():
        cnt+=1
        rmse+=math.pow((pred[k]-test[k]),2)

    rmse=math.sqrt(rmse/cnt)

    print "rmse=%s"%(rmse)


def RMSE2(pred=[],test=[]):
    rmse=0.0
    cnt=0.0001
    for i in range(len(pred)):
        rmse+=math.pow(pred[i]-test[i],2)
        cnt+=1

    rmse=math.sqrt(rmse/cnt)
    print "RMSE=%s"%(rmse)


def MAE(pred=None,test=None):
    mae=0.0
    cnt=0.00001
    for k in range(len(test)):
        cnt+=1
        mae+=math.fabs(pred[k]-test[k])

    mae/=cnt

    print "MAE=%s"%(mae)

def ClassifyError(pred=None,test=None):

    err_cnt=0.0
    threadhold=9.0

    for k in range(len(test)):
        if test[k]>=threadhold and pred[k]<threadhold or test[k]<threadhold and pred[k]>=threadhold :
            err_cnt+=1.0

    err_cnt/=len(test)


    print "ClassifyError=%s"%(err_cnt)







if __name__ == '__main__':

    """
        ret=readdata("test.txt")
        print ret
        for r in ret.keys():
            print r[0],r[1]

    """
    """
    genRankTest(train="/Users/nali/Eclipse/lenscy/bx_rating_explicit_train1.txt",
                test="/Users/nali/Eclipse/lenscy/bx_rating_explicit_test_rank1_6.0.txt",
                toTest="/Users/nali/Eclipse/lenscy/bx_rating_explicit_test1_for_rank.txt",
                toTestLines="/Users/nali/Eclipse/lenscy/bx_rating_explicit_test1_for_ranklines.txt",
                cols=[0, 1, 2])
    exit(0)
    """


    #globalAvg=readdata("/Users/nali/GitHub/librec/librec/demo/Results/GlobalAvg-rating-predictions.txt")
    itemAvg=readdata("/Users/nali/GitHub/librec/librec/demo/Results/ItemAvg-rating-predictions.txt")
    #random=readdata("/Users/nali/GitHub/librec/librec/demo/Results/Random-rating-predictions.txt")
    userAvg=readdata("/Users/nali/GitHub/librec/librec/demo/Results/UserAvg-rating-predictions.txt")
    svdPlusPlus=readdata("/Users/nali/GitHub/librec/librec/demo/Results/SVD++-rating-predictions.txt")
    svdPlusPlusFlip=readdata("/Users/nali/GitHub/librec/librec/demo/Results/SVD++_flip-rating-predictions.txt",[1,0,3])
    svd=readdata("/Users/nali/GitHub/librec/librec/demo/Results/SVD-rating-predictions.txt")
    test=readdata("/Users/nali/GitHub/librec/librec/demo/Results/UserAvg-rating-predictions.txt",[0,1,2])



    Hybrid(itemAvg=itemAvg,userAvg=userAvg,svdPlusPlus=svdPlusPlus,svdPlusPlusFlip=svdPlusPlusFlip,svd=svd,test=test );





