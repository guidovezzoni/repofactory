[![](https://jitpack.io/v/guidovezzoni/repofactory.svg)](https://jitpack.io/#guidovezzoni/repofactory)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/f1e73bb0ea4448ec84401e80b948e7b0)](https://www.codacy.com/app/guidovezzoni/repofactory?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=guidovezzoni/repofactory&amp;utm_campaign=Badge_Grade)

# RepoFactory
Tired of writing boiler plate code for implementing a standard repository pattern? Here is a flexible solution :-)

# Architecture

# Getting started
The idea behind the library is to provide a simple way to instantiate a repository pattern, providing a simple parameter to define the type of repository:

This is all is needed:
### 1) Instantiate the factory and declare a Repository<return_type, parameters>
```
    private final RepoFactory repoFactory = new RepoFactory();
    private final Repository<ForecastResponse, String> weatherForecastRepo;
```

### 2) Instantiate the repository defining the endpoint call
```
        weatherForecastRepo = repoFactory.createRepo(RepoType.SINGLE_LEVEL_CACHE,
                location -> interfaceApi
                        .getForecast(location);
```

### 2) Define the single to subscribe to
```
    public Single<ForecastResponse> getForecast(String location) {
        return weatherForecastRepo.get();
    }
```



## Gradle
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

## Sample usage




# Additional features
These features are candidate for next phase implementation:
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
