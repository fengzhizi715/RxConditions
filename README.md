# RxCondition
[![@Tony沈哲 on weibo](https://img.shields.io/badge/weibo-%40Tony%E6%B2%88%E5%93%B2-blue.svg)](http://www.weibo.com/fengzhizi715)
[![Download](https://img.shields.io/badge/Download-1.0.0-red.svg)](https://bintray.com/fengzhizi715/maven/rxcondition/_latestVersion)
[![GitHub release](https://img.shields.io/badge/release-1.0.0-blue.svg)](https://github.com/fengzhizi715/RxCondition/releases)
[![License](https://img.shields.io/badge/license-Apache%202-lightgrey.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)


通常而言，Rx如果遇到if条件语句、switch case语句时需要先选择分支条件，然后再进行链式调用。RxCondition产生的目的就是为了在这些情况下也能顺利地使用链式调用。

我在查找RxJava的条件、布尔操作符时，没有找到符合我需求的操作符。于是，我在网上找到了[RxJavaComputationExpressions](https://github.com/ReactiveX/RxJavaComputationExpressions)， 做了一些修改将RxJava1升级到RxJava2，增加了对Flowable的支持。

# 下载安装

Gradle:

```groovy
compile 'com.safframework:rxcondition:1.0.0'
```

Maven:

```groovy
<dependency>
  <groupId>com.safframework</groupId>
  <artifactId>rxcondition</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```


# 使用方法：
## 1.ifThen用法

if条件语句传统的写法：
```java
		Observable<String> observable = null;
		if (flag) {

			observable = Observable.create(new ObservableOnSubscribe<String>() {
				@Override
				public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
					e.onNext("this is true");
				}
			});
		} else {
			observable = Observable.create(new ObservableOnSubscribe<String>() {
				@Override
				public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
					e.onNext("this is false");
				}
			});
		}

		observable.subscribe(new Consumer<String>() {
			@Override
			public void accept(@NonNull String s) throws Exception {
				System.out.println("s="+s);
			}
		});
```

使用了ifThen()以后的写法：
```java
		Statement.ifThen(new BooleanSupplier() {
			@Override
			public boolean getAsBoolean() throws Exception {
				return flag;
			}
		}, Observable.create(new ObservableOnSubscribe<String>() {
			@Override
			public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
				e.onNext("this is true");
			}
		}),Observable.create(new ObservableOnSubscribe<String>() {
			@Override
			public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
				e.onNext("this is false");
			}
		})).subscribe(new Consumer<String>() {
			@Override
			public void accept(@NonNull String s) throws Exception {
				System.out.println("s="+s);
			}
		});
```
ifThen(BooleanSupplier condition, Observable<? extends R> then,Observable<? extends R> orElse)，其中第一个Observable是条件为true时执行的，第二个Observable则是条件为false时执行。

使用lambda表达式，进一步简化写法：
```java
        Statement.ifThen(()->{
             return flag;
        },Observable.create((e)->{
             e.onNext("this is true");
        }),Observable.create((e)->{
             e.onNext("this is false");
        })).subscribe((Consume) (s) -> {System.out.println("s="+s)});
```

当然，ifThen也支持Flowable：
```java
        Statement.ifThen(()->{
              return flag;
        },Flowable.just("this is true"), Flowable.just("this is false"))
        .subscribe((Consume) (s) -> {System.out.println("s="+s)});
```

## 2.switchCase用法

switch case语句传统的写法：
```java
		Flowable<String> flowable = null;
		switch(type) {
			case 0:
				flowable = Flowable.just("this is 0");
				break;
			case 1:
				flowable = Flowable.just("this is 1");
				break;
			case 2:
				flowable = Flowable.just("this is 2");
				break;
			case 3:
				flowable = Flowable.just("this is 3");
				break;

			default:
				flowable = Flowable.just("this is default");
				break;
		}
		flowable.subscribe(new Consumer<String>() {
			@Override
			public void accept(@NonNull String s) throws Exception {
				System.out.println("s="+s);
			}
		});
```

使用了switchCase()以后的写法：
```java
		Map<Integer,Flowable<String>> maps = new HashMap<>();
		maps.put(0,Flowable.just("this is 0"));
		maps.put(1,Flowable.just("this is 1"));
		maps.put(2,Flowable.just("this is 2"));
		maps.put(3,Flowable.just("this is 3"));
		
		Statement.switchCase(new Callable<Integer>() {

			@Override
			public Integer call() throws Exception {
				return type;
			}
		},maps,Flowable.just("this is default"))
		.subscribe(new Consumer<String>() {
			@Override
			public void accept(@NonNull String s) throws Exception {
				System.out.println("s="+s);
			}
		});
```
首先，将各个分支情况放入maps中。
其次，switchCase()的第一个参数是caseSelector，用于返回maps的key。最后一个参数是defaultCase，相当于switch case语句中的default语句。

改成lambda表达：
```java
		Map<Integer,Flowable<String>> maps = new HashMap<>();
		maps.put(0,Flowable.just("this is 0"));
		maps.put(1,Flowable.just("this is 1"));
		maps.put(2,Flowable.just("this is 2"));
		maps.put(3,Flowable.just("this is 3"));
		
		Statement.switchCase((Callable)()-> {return type;},maps,Flowable.just("this is default"))
		.subscribe((Consumer)(s) -> {System.out.println("s="+s);});
```

switchCase()中，第一个参数返回的是Map<K, R>中的key，它支持范型，所以switchCase()相对于switch case语句而已能够支持更多种类型。


License
-------

    Copyright (C) 2017 Tony Shen.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

