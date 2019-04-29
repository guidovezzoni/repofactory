[![](https://jitpack.io/v/guidovezzoni/repofactory.svg)](https://jitpack.io/#guidovezzoni/repofactory)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/f1e73bb0ea4448ec84401e80b948e7b0)](https://www.codacy.com/app/guidovezzoni/repofactory?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=guidovezzoni/repofactory&amp;utm_campaign=Badge_Grade)

# RepoFactory
Tired of writing boiler plate code for implementing a standard repository pattern? Here is a flexible solution for your repository pattern: you can either have a ready-made one or create one yourself.

# Getting started
The idea behind this library is to provide a simple way to instantiate a repository pattern, providing a parameter to define the type of repository.

The following steps describe how to implement a ready-made repository pattern. 

### 1) Define type of data involved:
a. model returned by the endpoint call, the same class will be used for caching the response - `<M>` in the code

b. any parameters required for the endpoint call - `<P>` in the code. It can be `Void` if no params are required. 
In case of multiple parameters, then `Pair` or `Triple` (from Kotlin or java) can be used, or any other _Tuple_ defined in other libraries. 
The only limitation is that methods `equals` and `hashCode` must be overridden.
This is because this class will be used as key for the `HashMap` that handles the cache.

c. type of repository chosen. Currently it can be:
* NO_CACHE - the repository simply triggers a network call for each request
* SINGLE_LEVEL_CACHE - a one level memory cache is present and will be used until the response associated with a specific value of parameters doesn't expire


### 2) Instantiate the factory and declare a `Repository<return_type, parameters>`
```
    private final RepoFactory repoFactory = new RepoFactory();
    private final Repository<ForecastResponse, String> weatherForecastRepo;
```

### 3) Instantiate the repository defining the endpoint call
```
        weatherForecastRepo = repoFactory.createRepo(RepoType.SINGLE_LEVEL_CACHE,
                location -> interfaceApi.getForecast(location);
```

### 4) Define the `Single` to which subscribe
```
    public Single<ForecastResponse> getForecast(String location) {
        return weatherForecastRepo.get();
    }
```

# Custom implementations

If any part of the architecture needs to be customised for specific needs - either the network datasource or the cache or the repository itself - the `RepoFactory` class can be derived and its method overridden as required.
See [RepoFactory.java](https://github.com/guidovezzoni/repofactory/blob/master/repofactory/src/main/java/com/guidovezzoni/repofactory/RepoFactory.java) for more info.

# Gradle
Add the JitPack repository in your root build.gradle at the end of repositories:
```
allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
```
Add the dependency
```
dependencies {
            implementation 'com.github.guidovezzoni:repofactory:0.1.2_alpha'
	}
```


# Additional features
These features are likely to be included in future releases:
* 3 level cache 
* periodical removal of expired cache

# Bugs and Feedback
For bugs, questions and discussions please use the [GitHub Issues](https://github.com/guidovezzoni/repofactory/issues) .

# History

###### version 0.1.2_alpha 29/04/2019

Minor changes, documentation improvements

###### version 0.1.1_alpha 28/04/2019

Fixed: network call always executed

###### version 0.1.0_alpha 26/04/2019

First alpha release

# License
```
   Copyright 2019 Guido Vezzoni

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
