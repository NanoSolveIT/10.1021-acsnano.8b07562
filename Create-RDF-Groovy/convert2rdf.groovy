@Grab(group='io.github.egonw.bacting', module='managers-ui', version='0.0.10')
@Grab(group='io.github.egonw.bacting', module='managers-rdf', version='0.0.10')
@Grab(group='io.github.egonw.bacting', module='managers-excel', version='0.0.10')

workspaceRoot = ".."
ui = new net.bioclipse.managers.UIManager(workspaceRoot);
rdf = new net.bioclipse.managers.RDFManager(workspaceRoot);
bioclipse = new net.bioclipse.managers.BioclipseManager(workspaceRoot);
excel = new net.bioclipse.managers.ExcelManager(workspaceRoot);

inputFilename = "/Create-RDF-Groovy/nn8b07562_si_001.xlsx"
outputFilename = "/Create-RDF-Groovy/nn8b07562_si_001.ttl"
logFilename = "/Create-RDF-Groovy/conversion.log"
logMessages = ""

baoNS = "http://www.bioassayontology.org/bao#"
citoNS = "http://purl.org/net/cito/"
dcNS = "http://purl.org/dc/elements/1.1/"
dctNS = "http://purl.org/dc/terms/"
doiNS = "https://doi.org/"
enmNS = "http://purl.enanomapper.org/onto/"
metaNS = "http://egonw.github.com/enmrdf/meta-analysis/"
npoNS = "http://purl.bioontology.org/ontology/npo#"
oboNS = "http://purl.obolibrary.org/obo/"
ssoNS = "http://semanticscience.org/resource/"
voidNS = "http://rdfs.org/ns/void#"
xsdNS = "http://www.w3.org/2001/XMLSchema#"
owlNS = "http://www.w3.org/2002/07/owl#"

materials = new java.util.HashMap()
articles = new java.util.HashSet()

rdfType = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"
rdfsLabel = "http://www.w3.org/2000/01/rdf-schema#label"
chebi59999 = "http://purl.obolibrary.org/obo/CHEBI_59999"

store = rdf.createInMemoryStore()
rdf.addPrefix(store, "bao", baoNS)
rdf.addPrefix(store, "cito", citoNS)
rdf.addPrefix(store, "dc", dcNS)
rdf.addPrefix(store, "dct", dctNS)
rdf.addPrefix(store, "enm", enmNS)
rdf.addPrefix(store, "meta", metaNS)
rdf.addPrefix(store, "npo", npoNS)
rdf.addPrefix(store, "obo", oboNS)
rdf.addPrefix(store, "owl", owlNS)
rdf.addPrefix(store, "sso", ssoNS)
rdf.addPrefix(store, "void", voidNS)

nanomaterials = [
  "Ag" : [
    iri : "http://purl.bioontology.org/ontology/npo#NPO_1384",
    core : [ label : "silver", smiles : "[Ag]" ]
  ],
  "Al2O3" : [
    iri: "http://purl.enanomapper.org/onto/ENM_9000005",
    core : [ label : "Al2O3" ]
  ],
  "Au" : [
    iri : "http://purl.bioontology.org/ontology/npo#NPO_401",
    core : [ label : "silver", smiles : "[Au]" ]
  ],
  "CeO2" : [
    iri : "http://purl.enanomapper.org/onto/ENM_9000006",
    core : [ label : "CeO2", smiles : "O=[Ce]=O" ]
  ],
  "Chitosan" : [
    iri : "http://purl.bioontology.org/ontology/npo#NPO_261"
  ],
  "CuO" : [
    iri : "http://purl.bioontology.org/ontology/npo#NPO_1544",
    core : [ label : "CuO", smiles : "[Cu]=O" ]
  ],
  "Cu2O" : [
    iri : "http://purl.obolibrary.org/obo/CHEBI_134402",
    core : [ label : "Cu2O", smiles : "[Cu]O[Cu]" ]
  ],
  "Carbon Nanotubes" : [
    iri : "http://purl.bioontology.org/ontology/npo#NPO_943",
    core : [ label : "carbon", smiles : "[C]" ]
  ],
  "Hydroxyapatite" : [
    iri : "http://purl.bioontology.org/ontology/npo#NPO_1568"
  ],
  "Iron oxide" : [
    iri : "http://purl.bioontology.org/ontology/npo#NPO_729",
    core : [ label : "iron oxide" ]
  ],
  "Liposomes" : [
    iri: "http://purl.bioontology.org/ontology/npo#NPO_719"
  ],
  "Polystyrene" : [
    iri: "http://purl.enanomapper.org/onto/ENM_9000008"
  ],
  "QDs" : [
    iri : "http://purl.bioontology.org/ontology/npo#NPO_589",
  ],
  "SiO2" : [
    iri : "http://purl.bioontology.org/ontology/npo#NPO_1373",
    core : [ label : "SiO2", smiles : "O=[Si]=O" ]
  ],
  "TiO2" : [
    iri : "http://purl.obolibrary.org/obo/CHEBI_51050",
    core : [ label : "TiO2", smiles : "O=[Ti]=O" ]
  ],
  "ZnO" : [
    iri : "http://purl.bioontology.org/ontology/npo#NPO_1542",
    core : [ label : "ZnO", smiles : "[Zn]=O" ]
  ]
]

coatings = [
  "l-cysteine" : [
    label : "l-cysteine",
    smiles : "N[C@@H](CS)C(O)=O"
  ],
  "l-lysine" : [
    label : "l-lysine",
    smiles : "NCCCC[C@H](N)C(O)=O"
  ],
  "COOH" : [
    label : "COOH",
    smiles : "C(=O)O"
  ],
  "Citrate" : [
    label : "citrate",
    smiles : "C(C(=O)O)C(CC(=O)O)(C(=O)O)O"
  ]
]

datasetIRI = "${metaNS}dataset"
rdf.addObjectProperty(store, datasetIRI, rdfType, "${voidNS}Dataset")
rdf.addDataProperty(store, datasetIRI, "${dctNS}title", "Meta-Analysis RDF")
rdf.addDataProperty(store, datasetIRI, "${dctNS}description", "RDF version of the data from ACS Nano. 2019, 21:2, 1583-1594. doi:10.1021/ACSNANO.8B07562.")
rdf.addDataProperty(store, datasetIRI, "${dctNS}publisher", "Egon Willighagen (H2020 NanoSolveIT)")
rdf.addObjectProperty(store, datasetIRI, "${dctNS}license", "https://creativecommons.org/publicdomain/zero/1.0/")

assayCount = 0;
toxCount = 0;
citations = 0;
articleCounter = 0
data = excel.getSheet(inputFilename, 0, true)
//for (i in 1..data.rowCount) {
for (i in 1..500) {
  newMaterial = false
  
  // the nanomaterial
  name = data.get(i, "Nanoparticle")
  coating = data.get(i, "coat")

  // the diameter
  diameter = data.get(i, "Diameter (nm)")
  // the zeta potential
  zp = data.get(i, "Zeta potential (mV)")

  // literature
  artTitle = data.get(i, "Reference DOI")

  // unique id
  if (data.get(i, "Particle ID") == "") continue
  uniqueKey = Double.valueOf(data.get(i, "Particle ID")).intValue()

  if (materials.containsKey(uniqueKey)) {
    materialCounter = materials.get(uniqueKey)
  } else {
    newMaterial = true;
    materialCounter = uniqueKey;
    materials.put(uniqueKey, uniqueKey);
  }
  
  doi = artTitle.trim()
  if (!articles.contains(doi)) {
    if (doi.startsWith("10.")) {
      articleCounter++
      articles.add(doi)
      artIRI = "${metaNS}ref$articleCounter"
      rdf.addDataProperty(store, artIRI, "${dcNS}title" , doi)
      rdf.addObjectProperty(store, artIRI, "${owlNS}sameAs" , "https://doi.org/${doi}")
    }
  }

  if (newMaterial) {
    if (nanomaterials[name]) {
      // the next material
      enmIRI = "${metaNS}m$materialCounter"
      rdf.addObjectProperty(store, enmIRI, rdfType, chebi59999)
      rdf.addObjectProperty(store, enmIRI, "${dctNS}source" , datasetIRI)
      rdf.addDataProperty(store, enmIRI, rdfsLabel, name)

      // the components (they all have a core)
      coreIRI = "${enmIRI}_core"
      rdf.addObjectProperty(store, enmIRI, "${npoNS}has_part", coreIRI)
      rdf.addObjectProperty(store, coreIRI, rdfType, "${npoNS}NPO_1597")

      if (nanomaterials[name].iri) {
        rdf.addObjectProperty(store, enmIRI, "${dctNS}type", nanomaterials[name].iri)
      }
      if (nanomaterials[name].core && nanomaterials[name].core.smiles) {
        smilesIRI = "${coreIRI}_smiles"
        rdf.addObjectProperty(store, coreIRI, "${ssoNS}CHEMINF_000200", smilesIRI)
        rdf.addObjectProperty(store, smilesIRI, rdfType, "${ssoNS}CHEMINF_000018")
        rdf.addDataProperty(store, smilesIRI, "${ssoNS}SIO_000300", nanomaterials[name].core.smiles)
        rdf.addDataProperty(store, smilesIRI, rdfsLabel, nanomaterials[name].core.label)
      }

      if (coating) {
        coatingComponents = coating.split(" ")
        for (component in coatingComponents) {
          if (coatings[component]) {
            coatingIRI = "${enmIRI}_coating"
            rdf.addObjectProperty(store, enmIRI, "${npoNS}has_part", coatingIRI)
            smilesIRI = "${coatingIRI}_smiles"
            rdf.addObjectProperty(store, coatingIRI, "${ssoNS}CHEMINF_000200", smilesIRI)
            rdf.addObjectProperty(store, smilesIRI, rdfType, "${ssoNS}CHEMINF_000018")
            rdf.addDataProperty(store, smilesIRI, "${ssoNS}SIO_000300", coatings[component].smiles)
            rdf.addDataProperty(store, smilesIRI, rdfsLabel, coatings[component].label)
          } else {
            logMessages += "Unrecognized coating component: $component\n"
          }
        }
      }

      if (diameter && !diameter.contains("N/A") && !diameter.contains("(")) {
        diameter = diameter.replace(",", ".")
                           .replace("~", "")
                           .trim()
      
        assayCount++;
        assayIRI = "${enmIRI}_sizeAssay" + assayCount
        measurementGroupIRI = "${enmIRI}_sizeMeasurementGroup" + assayCount
        endpointIRI = "${measurementGroupIRI}_sizeEndpoint"

        // the assay
        rdf.addObjectProperty(store, assayIRI, rdfType, "${baoNS}BAO_0000015")
        rdf.addDataProperty(store, assayIRI, "${dcNS}title", "Diameter Assay")
        rdf.addObjectProperty(store, assayIRI, "${baoNS}BAO_0000209", measurementGroupIRI)
        rdf.addObjectProperty(store, assayIRI, "${dctNS}source", artIRI)
        rdf.addObjectProperty(store, enmIRI, "${oboNS}BFO_0000056", measurementGroupIRI)

        // the measurement group
        rdf.addObjectProperty(store, measurementGroupIRI, rdfType, "${baoNS}BAO_0000040")
        rdf.addObjectProperty(store, measurementGroupIRI, "${oboNS}OBI_0000299", endpointIRI)

        // the endpoint
        rdf.addObjectProperty(store, endpointIRI, rdfType, "${npoNS}NPO_1539")
        rdf.addObjectProperty(store, endpointIRI, "${oboNS}IAO_0000136", enmIRI)
        rdf.addDataProperty(store, endpointIRI, rdfsLabel, "Diameter")
        if (doi.startsWith("10."))
          rdf.addObjectProperty(store, endpointIRI, "${citoNS}usesDataFrom", "https://doi.org/${doi}")
 
        if (diameter.contains("-") || diameter.contains("–")) {
          diameter = diameter.replace("–","-")
          if (excelCorrections.containsKey(diameter.trim().toLowerCase())) {
            logMessages += "Replaced " + diameter + " with "
            diameter = excelCorrections.get(diameter.trim().toLowerCase())
            logMessages += diameter + "\n"
          }
          rdf.addTypedDataProperty(store, endpointIRI, "${oboNS}STATO_0000035", diameter, "${xsdNS}string")
          rdf.addDataProperty(store, endpointIRI, "${ssoNS}has-unit", "nm")
        } else if (diameter.contains("±")) {
          rdf.addTypedDataProperty(store, endpointIRI, "${oboNS}STATO_0000035", diameter, "${xsdNS}string")
          rdf.addDataProperty(store, endpointIRI, "${ssoNS}has-unit", "nm")
        } else if (diameter.trim().startsWith("<")) {
          rdf.addTypedDataProperty(store, endpointIRI, "${oboNS}STATO_0000035", "1-$diameter", "${xsdNS}string")
          rdf.addDataProperty(store, endpointIRI, "${ssoNS}has-unit", "nm")
        } else if (diameter.contains("<")) {
          logMessages += "Unrecognized diameter value: $diameter \n"
        } else if (diameter.contains(";")) {
          logMessages += "Unrecognized diameter value: $diameter \n"
        } else {
          rdf.addTypedDataProperty(store, endpointIRI, "${ssoNS}has-value", diameter, "${xsdNS}double")
          rdf.addDataProperty(store, endpointIRI, "${ssoNS}has-unit", "nm")
        }
      }
  
      // the zeta potential
      if (!zp) continue
      zp = zp.replace("−", "-")
    
      if (zp && !zp.contains("N/A") &&  zp != "positive" && !zp.contains("(") && !zp.contains("at") &&
          !zp.contains("-------------------")) {
        zp = zp.replace("mV","").trim()
        assayCount++
        assayIRI = "${enmIRI}_zpAssay" + assayCount
        measurementGroupIRI = "${enmIRI}_zpMeasurementGroup" + assayCount
        endpointIRI = "${measurementGroupIRI}_zpEndpoint"
  
        // the assay
        rdf.addObjectProperty(store, assayIRI, rdfType, "${baoNS}BAO_0000015")
        rdf.addDataProperty(store, assayIRI, "${dcNS}title", "Zeta potential")
        rdf.addObjectProperty(store, assayIRI, "${baoNS}BAO_0000209", measurementGroupIRI)
        rdf.addObjectProperty(store, assayIRI, "${dctNS}source", artIRI)
        rdf.addObjectProperty(store, enmIRI, "${oboNS}BFO_0000056", measurementGroupIRI)
  
        // the measurement group
        rdf.addObjectProperty(store, measurementGroupIRI, rdfType, "${baoNS}BAO_0000040")
        rdf.addObjectProperty(store, measurementGroupIRI, "${oboNS}OBI_0000299", endpointIRI)
  
        // the endpoint
        rdf.addObjectProperty(store, endpointIRI, rdfType, "${npoNS}NPO_1302")
        rdf.addObjectProperty(store, endpointIRI, "${oboNS}IAO_0000136", enmIRI)
        rdf.addDataProperty(store, endpointIRI, rdfsLabel, "Zeta potential")
        if (doi.startsWith("10."))
          rdf.addObjectProperty(store, endpointIRI, "${citoNS}usesDataFrom", "https://doi.org/${doi}")

     
        zp = zp.replace(",", ".")
        zp = zp.replace("ca", "").trim()
        zp = zp.replace("- 51.6", "-51.6").trim()
        if (zp.substring(1).contains("-")) {
          if (excelCorrections.containsKey(zp.trim().toLowerCase())) {
            // print("Replaced " + zp + " with ")
            zp = excelCorrections.get(zp.trim().toLowerCase())
            // println(zp)
          }
          rdf.addTypedDataProperty(store, endpointIRI, "${oboNS}STATO_0000035", zp, "${xsdNS}string")
          rdf.addDataProperty(store, endpointIRI, "${ssoNS}has-unit", "mV")
        } else if (zp.contains("...")) {
          zp = zp.replace("...","-")
          rdf.addTypedDataProperty(store, endpointIRI, "${oboNS}STATO_0000035", zp, "${xsdNS}string")
          rdf.addDataProperty(store, endpointIRI, "${ssoNS}has-unit", "mV")
        } else if (zp.contains("±")) {
          rdf.addTypedDataProperty(store, endpointIRI, "${oboNS}STATO_0000035", zp, "${xsdNS}string")
          rdf.addDataProperty(store, endpointIRI, "${ssoNS}has-unit", "mV")
        } else if (zp.contains("<")) {
          logMessages += "Unrecognized zeta potential value: $zp \n"
        } else {
          rdf.addTypedDataProperty(store, endpointIRI, "${ssoNS}has-value", zp, "${xsdNS}double")
          rdf.addDataProperty(store, endpointIRI, "${ssoNS}has-unit", "mV")
        }
      }

    } else {
      logMessages += "Unrecognized material type: $name\n"
      continue
    }
  }

}

if (ui.fileExists(logFilename)) ui.remove(logFilename)
logfile = ui.newFile(logFilename, logMessages )

if (ui.fileExists(outputFilename)) ui.remove(outputFilename)
output = ui.newFile(outputFilename, rdf.asTurtle(store) )

println "Materials: $materialCounter"
println "Assays: $assayCount"
println "  of which TOX: $toxCount"
println "Citations: $articleCounter"


