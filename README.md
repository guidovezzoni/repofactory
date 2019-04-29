[![](https://jitpack.io/v/guidovezzoni/repofactory.svg)](https://jitpack.io/#guidovezzoni/repofactory)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/f1e73bb0ea4448ec84401e80b948e7b0)](https://www.codacy.com/app/guidovezzoni/repofactory?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=guidovezzoni/repofactory&amp;utm_campaign=Badge_Grade)

# RepoFactory
Tired of writing boiler plate code for implementing a standard repository pattern? Here is a flexible solution :-)

# Architecture

# Getting started
The idea behind the library is to provide a simple way to instantiate a repository pattern, providing a simple parameter to define the type of repository:

This is all is needed:

### 1) Define type of data involved:
a. model returned by the endpoint call - `<M>` in the code

b. parameter required for the endpoint call - `<P>` in the code. If multiple parameters are required, then [Pair] or [Triple] can be used, or any other Tuple. The only limitation is that [Object.equals] and [Object.hashCode] must be overridden.
This is because instances will be used as key for the HashMap that handles the cache.

c. type of repository chosen. Currently it's:

* NO_CACHE - the repository simply triggers a network call for each request

* SINGLE_LEVEL_CACHE - a one level memory cache is present and will be used until the response associated with a specific value of parameters doesn't expire
 

### 2) Instantiate the factory and declare a Repository<return_type, parameters>
```
    private final RepoFactory repoFactory = new RepoFactory();
    private final Repository<ForecastResponse, String> weatherForecastRepo;
```

### 3) Instantiate the repository defining the endpoint call
```
        weatherForecastRepo = repoFactory.createRepo(RepoType.SINGLE_LEVEL_CACHE,
                location -> interfaceApi
                        .getForecast(location);
```

### 4) Define the single to subscribe to
```
    public Single<ForecastResponse> getForecast(String location) {
        return weatherForecastRepo.get();
    }
```

# Custom implementation

If any part of the system needs to be customised - the network datasource, the cache or the repository itself - RepoFactory can be derived and it's method overridden as required.

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
