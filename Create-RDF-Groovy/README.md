# Convertion to ENMRDF

This folder contains a Bioclipse script to convert the content from the database
into ENMRDF.

```shell
groovy convert2rdf.groovy
```

A simple way to test the resulting Turtle is to try to convert it to N-triples
(which fail on reading the Turtle if something is wrong):

```shell
rapper -o ntriples -i turtle nn8b07562_si_001.ttl  > nn8b07562_si_001.nt
```

To get some summarizing statistics:

```shell
roqet -r tsv materialCount.rq -D nn8b07562_si_001.ttl
roqet -r tsv articleCount.rq -D nn8b07562_si_001.ttl
roqet -r tsv bioAssayCount.rq -D nn8b07562_si_001.ttl
roqet -r tsv bioAssayTypeCount.rq -D nn8b07562_si_001.ttl
```

## How eNanoMapper sees your data

The eNanoMapper database can read Turtle and it could be useful to see what
it recognizes. This [folder](https://github.com/vedina/loom/tree/master/loom-nm/src/main/resources/net/idea/loom/nm/enmrdf)
contains copies of the SPARQL queries it runs on the Turtle to recognize content.

It can be helpful to run these against the Turtle to learn
how it is seen by eNanoMapper. After putthing them in a `ambit/` folder,
and removing the `BIND` lines, we can run:

```shell
roqet -r tsv ambit/bundles_all.sparql -D nn8b07562_si_001.ttl
roqet -r tsv ambit/m_allmaterials.sparql -D nn8b07562_si_001.ttl
roqet -r tsv ambit/m_coating.sparql -D nn8b07562_si_001.ttl
roqet -r tsv ambit/m_materialsprops.sparql -D nn8b07562_si_001.ttl
roqet -r tsv ambit/m_sparql.sparql -D nn8b07562_si_001.ttl
```
