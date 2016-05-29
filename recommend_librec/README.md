这个模块基于librec开发，原来的KNN,SlopOne算法在数据量大的时候内存不足。所以使用guava cache全部改造了下。另外添加了SVD-KNN算法，即先做SVD分解，然后使用UserKNN,ItemKNN。

另外rating test,和rank test都修改为自己设计的三个测试方案。

所有的测试用例都在test目录下面，配置文件在demo目录下面。每个算法都对应AlgothmTest.java里面的一个方法。


