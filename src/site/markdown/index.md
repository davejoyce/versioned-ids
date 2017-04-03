# Versioned IDs

Versioned IDs are namespace-aware identifier types that support simple value incrementation while storing a high degree
of information. They offer application developers an easy way to get started building complex, version-sensitive domain
models with strong IDs that are:

* Categorical
* Human-readble
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
    <version>1.0-SNAPSHOT</version>
</dependency>
```

2. Next, choose one of the 3 basic ID types depending upon the level of time-sensitivity in your domain model.

| You need: | Choose:  |
| -------- | -------- |
| categorical organization of IDs | [NamespaceId\<T\>](examples.html#Using_NamespaceId) |
| ...and storage of creation/effective time | TemporalNamespaceId\<T\> |
| ...and storage of update/fixing time | BiTemporalNamespaceId\<T\> |

3. Finally, does the type you selected store enough information, or do you need a bit more richness? All of these types
support extension and composition!