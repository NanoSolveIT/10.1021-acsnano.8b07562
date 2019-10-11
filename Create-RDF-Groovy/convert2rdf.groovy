@Grab(group='io.github.egonw.bacting', module='managers-ui', version='0.0.10-SNAPSHOT')
@Grab(group='io.github.egonw.bacting', module='managers-rdf', version='0.0.10-SNAPSHOT')
@Grab(group='io.github.egonw.bacting', module='managers-excel', version='0.0.10-SNAPSHOT')

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
etoxNS = "http://egonw.github.com/enmrdf/nanoe-tox/"
npoNS = "http://purl.bioontology.org/ontology/npo#"
oboNS = "http://purl.obolibrary.org/obo/"
ssoNS = "http://semanticscience.org/resource/"
voidNS = "http://rdfs.org/ns/void#"
xsdNS = "http://www.w3.org/2001/XMLSchema#"

materials = new java.util.HashMap()

rdfType = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"
rdfsLabel = "http://www.w3.org/2000/01/rdf-schema#label"
chebi59999 = "http://purl.obolibrary.org/obo/CHEBI_59999"

store = rdf.createInMemoryStore()
rdf.addPrefix(store, "bao", baoNS)
rdf.addPrefix(store, "cito", citoNS)
rdf.addPrefix(store, "dc", dcNS)
rdf.addPrefix(store, "dct", dctNS)
rdf.addPrefix(store, "enm", enmNS)
rdf.addPrefix(store, "etox", etoxNS)
rdf.addPrefix(store, "npo", npoNS)
rdf.addPrefix(store, "obo", oboNS)
rdf.addPrefix(store, "sso", ssoNS)
rdf.addPrefix(store, "void", voidNS)

nanomaterials = [
  "CeO2" : [
    iri : "http://purl.enanomapper.org/onto/ENM_9000006",
    core : [ label : "CeO2", smiles : "O=[Ce]=O" ]
  ],
  "CuO" : [
    iri : "http://purl.bioontology.org/ontology/npo#NPO_1544",
    core : [ label : "CuO", smiles : "[Cu]=O" ]
  ],
  "ZnO" : [
    iri : "http://purl.bioontology.org/ontology/npo#NPO_1542",
    core : [ label : "ZnO", smiles : "[Zn]=O" ]
  ]
]


datasetIRI = "${etoxNS}dataset"
rdf.addObjectProperty(store, datasetIRI, rdfType, "${voidNS}Dataset")
rdf.addDataProperty(store, datasetIRI, "${dctNS}title", "Meta-Analysis RDF")
rdf.addDataProperty(store, datasetIRI, "${dctNS}description", "RDF version of the data from ACS Nano. 2019, 21:2, 1583-1594. doi:10.1021/ACSNANO.8B07562.")
rdf.addDataProperty(store, datasetIRI, "${dctNS}publisher", "Egon Willighagen (H2020 NanoSolveIT)")
rdf.addObjectProperty(store, datasetIRI, "${dctNS}license", "https://creativecommons.org/publicdomain/zero/1.0/")

assayCount = 0;
toxCount = 0;
citations = 0;
data = excel.getSheet(inputFilename, 0, true)
for (i in 1..data.rowCount) {
  newMaterial = false
  
  // the name
  name = data.get(i, "Nanoparticle")
  // the diameter
  diameter = data.get(i, "Diameter (nm)")
  // the zeta potential
  zp = data.get(i, "Zeta potential (mV)")

  // literature
  artTitle = data.get(i, "Reference DOI")

  // unique id
  uniqueKey = data.get(i, "Particle ID")

  if (materials.containsKey(uniqueKey)) {
    materialCounter = materials.get(uniqueKey)
  } else {
    newMaterial = true;
    materialCounter = uniqueKey;
    materials.put(uniqueKey, uniqueKey);
  }

  doi = artTitle.trim()

  // the next material
  enmIRI = "${etoxNS}m$materialCounter"
  rdf.addObjectProperty(store, enmIRI, rdfType, chebi59999)
  rdf.addObjectProperty(store, enmIRI, "${dctNS}source" , datasetIRI)
  rdf.addDataProperty(store, enmIRI, rdfsLabel, name)

  // the components (they all have a core)
  coreIRI = "${enmIRI}_core"
  rdf.addObjectProperty(store, enmIRI, "${npoNS}has_part", coreIRI)
  rdf.addObjectProperty(store, coreIRI, rdfType, "${npoNS}NPO_1597")

  if (newMaterial) {
    if (nanomaterials[name]) {
      if (nanomaterials[name].iri) {
        rdf.addObjectProperty(store, enmIRI, "${dctNS}type", nanomaterials[name].iri)
      } else {
        logMessages += "Unrecognized material type: $name\n"
      }
      if (nanomaterials[name].core && nanomaterials[name].core.smiles) {
        smilesIRI = "${coreIRI}_smiles"
        rdf.addObjectProperty(store, coreIRI, "${ssoNS}CHEMINF_000200", smilesIRI)
        rdf.addObjectProperty(store, smilesIRI, rdfType, "${ssoNS}CHEMINF_000018")
        rdf.addDataProperty(store, smilesIRI, "${ssoNS}SIO_000300", nanomaterials[name].core.smiles)
        rdf.addDataProperty(store, smilesIRI, rdfsLabel, nanomaterials[name].core.label)
      }
      if (nanomaterials[name].coating) {
        coatingIRI = "${enmIRI}_coating"
        rdf.addObjectProperty(store, enmIRI, "${npoNS}has_part", coatingIRI)
        smilesIRI = "${coatingIRI}_smiles"
        rdf.addObjectProperty(store, coatingIRI, "${ssoNS}CHEMINF_000200", smilesIRI)
        rdf.addObjectProperty(store, smilesIRI, rdfType, "${ssoNS}CHEMINF_000018")
        rdf.addDataProperty(store, smilesIRI, "${ssoNS}SIO_000300", nanomaterials[name].coating.smiles)
        rdf.addDataProperty(store, smilesIRI, rdfsLabel, nanomaterials[name].coating.label)
      }
    } else {
      logMessages += "Unrecognized material type: $name\n"
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
      // rdf.addObjectProperty(store, assayIRI, rdfType, "${baoNS}BAO_0000015")
      // rdf.addDataProperty(store, assayIRI, "${dcNS}title", "Diameter Assay")
      // rdf.addObjectProperty(store, assayIRI, "${baoNS}BAO_0000209", measurementGroupIRI)
      rdf.addObjectProperty(store, enmIRI, "${oboNS}BFO_0000056", measurementGroupIRI)

      // the measurement group
      rdf.addObjectProperty(store, measurementGroupIRI, rdfType, "${baoNS}BAO_0000040")
      rdf.addObjectProperty(store, measurementGroupIRI, "${oboNS}OBI_0000299", endpointIRI)

      // the endpoint
      rdf.addObjectProperty(store, endpointIRI, rdfType, "${npoNS}NPO_1539")
      rdf.addObjectProperty(store, endpointIRI, "${oboNS}IAO_0000136", enmIRI)
      rdf.addDataProperty(store, endpointIRI, rdfsLabel, "Diameter")
 
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
      // rdf.addObjectProperty(store, assayIRI, rdfType, "${baoNS}BAO_0000015")
      // rdf.addDataProperty(store, assayIRI, "${dcNS}title", "Zeta potential")
      // rdf.addObjectProperty(store, assayIRI, "${baoNS}BAO_0000209", measurementGroupIRI)
      rdf.addObjectProperty(store, enmIRI, "${oboNS}BFO_0000056", measurementGroupIRI)
  
      // the measurement group
      rdf.addObjectProperty(store, measurementGroupIRI, rdfType, "${baoNS}BAO_0000040")
      rdf.addObjectProperty(store, measurementGroupIRI, "${oboNS}OBI_0000299", endpointIRI)
  
      // the endpoint
      rdf.addObjectProperty(store, endpointIRI, rdfType, "${npoNS}NPO_1302")
      rdf.addObjectProperty(store, endpointIRI, "${oboNS}IAO_0000136", enmIRI)
      rdf.addDataProperty(store, endpointIRI, rdfsLabel, "Zeta potential")
     
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

  }

}

if (ui.fileExists(logFilename)) ui.remove(logFilename)
logfile = ui.newFile(logFilename, logMessages )

if (ui.fileExists(outputFilename)) ui.remove(outputFilename)
output = ui.newFile(outputFilename, rdf.asTurtle(store) )

println "Materials: $materialCounter"
println "Assays: $assayCount"
println "  of which TOX: $toxCount"
println "Citations: $citations"


