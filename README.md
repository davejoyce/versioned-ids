# Versioned IDs

[![Build Status](https://travis-ci.org/davejoyce/versioned-ids.svg?branch=master)](https://travis-ci.org/davejoyce/versioned-ids)
[![codecov.io](http://codecov.io/github/davejoyce/versioned-ids/coverage.svg?branch=master)](https://codecov.io/gh/davejoyce/versioned-ids/branch/master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.davejoyce/versioned-ids/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.davejoyce/versioned-ids/badge.svg)

Versioned IDs are namespace-aware identifiers that support simple value incrementation while storing a high degree
of information. They offer application developers an easy way to build complex, version-sensitive domain models
with strong IDs that are:

* Categorical
* Human-readable
* Incrementable
* Immutable
* Serializable
* Sortable
* Time-specific (to nanosecond)

## Getting Started

1. Start by including the Versioned IDs library in your project. Here's an example of including it as a Maven compile
dependency:

```xml
<dependency>
    <groupId>io.github.davejoyce</groupId>
    <artifactId>versioned-ids</artifactId>
    <version>1.1.1</version>
</dependency>
```

2. Choose one of the 3 basic ID types depending upon the level of time-sensitivity in your domain model.

| You need: | Choose:  |
| -------- | -------- |
| categorical organization of IDs | NamespaceId\<T\> |
| ...and storage of creation/effective time | TemporalNamespaceId\<T\> |
| ...and storage of update/fixing time | BiTemporalNamespaceId\<T\> |

3. Does the type you selected store enough information, or do you need more? The classes in this library
   support extension and composition!

## Documentation

Project documentation (including API documentation)

* [Latest Release documentation](https://davejoyce.github.io/versioned-ids/release/)
* [Current Snapshot documentation](https://davejoyce.github.io/versioned-ids/snapshot/)

## Bugs and Feedback

For bugs, questions, and discussions please use the [project's issues](https://github.com/davejoyce/versioned-ids/issues).

## License

Copyright 2017 David Joyce

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

<http://www.apache.org/licenses/LICENSE-2.0>

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
