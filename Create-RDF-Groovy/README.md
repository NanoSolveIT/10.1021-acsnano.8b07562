Convertion to ENMRDF
====================

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
