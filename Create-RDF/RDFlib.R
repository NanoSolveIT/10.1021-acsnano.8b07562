### Maastricht University - BiGCaT
### Laurent Winckers
### 2019-10-03

# load libraries
library(rdflib)
library(xlsx)

# set working directory to where script is saved
setwd(dirname(rstudioapi::getActiveDocumentContext()$path))
getwd()

# load excel file
excel <- read.xlsx2("./data-input/nn8b07562_si_001.xlsx", 1)

# create empty rdf
rdf <- rdf()

# function to add rdf triples
create_rdf <- function(subject, predicate, object){
  for (i in 1:length(subject)) {
  rdf_add(rdf, subject = paste0(subject[i]), predicate = predicate, object = object[i])
  }
}

# loop over columns of excel file and add them in rdf using function
x <- 1
repeat {
  create_rdf(subject = excel$Nanoparticle, predicate = colnames(excel)[x], object = excel[,x])
  x = x+1
  if (x == 25){
    break
  }
}

# format rdf to ttl file
options(rdf_print_format = "turtle")

# view rdf
rdf
