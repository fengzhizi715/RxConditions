# RxCondition
[![@Tony沈哲 on weibo](https://img.shields.io/badge/weibo-%40Tony%E6%B2%88%E5%93%B2-blue.svg)](http://www.weibo.com/fengzhizi715)
[![Download](https://img.shields.io/badge/Download-1.0.0-red.svg)](https://bintray.com/fengzhizi715/maven/rxcondition/_latestVersion)
[![License](https://img.shields.io/badge/license-Apache%202-lightgrey.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)


# 目的
通常而言，if条件语句、switch case语句会破坏链式调用。这个库产生的目的就是为了避免在这些情况下中断链式调用。
我最初找到了[RxJavaComputationExpressions](https://github.com/ReactiveX/RxJavaComputationExpressions)， 由于它历史有点久远并且是使用Rxjava1，
于是我将RxJava1升级到Rxjava2，支持了Flowable，并做了一些优化。


# 下载安装
Gradle:

```groovy
compile 'tony-common:rxcondition:1.0.0'
```

Maven:

```groovy
<dependency>
  <groupId>tony-common</groupId>
  <artifactId>rxcondition</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```

# 使用方法：
## 1.ifThen用法

if 条件下传统的写法：
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

使用了ifThen以后的写法：
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

更进一步，使用lambda表达式：
```java
        Statement.ifThen(()->{
             return flag;
        },Observable.create((e)->{
             e.onNext("this is true");
        }),Observable.create((e)->{
             e.onNext("this is false");
        })).subscribe((Consume) (s) -> {System.out.println("s="+s)});
```

ifThen还可以支持Flowable：
```java
        Statement.ifThen(()->{
              return flag;
        },Flowable.just("this is true"), Flowable.just("this is false"))
        .subscribe((Consume) (s) -> {System.out.println("s="+s)});
```


## 2.switchCase用法


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

