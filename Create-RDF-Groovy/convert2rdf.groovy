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
biboNS = "http://purl.org/ontology/bibo/"
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
rdf.addPrefix(store, "bibo", biboNS)
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

bioassays = [
  "Alamar blue" : [
    iri : "${baoNS}BAO_0010042" // general metabolomic profiling assay
  ],
  "Alamar Blue" : [
    iri : "${baoNS}BAO_0010042" // general metabolomic profiling assay
  ],
  "ApoTox­Glo™ Triplex" : [
    iri : "${baoNS}BAO_0002764"
  ],
  "ATP" : [
    iri : "${baoNS}BAO_0010001"
  ],
  "ATPLite" : [
    iri : "${baoNS}BAO_0010001"
  ],
  "CellTiter­Blue" : [
    iri : "${baoNS}BAO_0140012" // FIXME: really a kit
  ],
  "CellTiter­Glo" : [
    iri : "${baoNS}BAO_00030099" // general cell viability assay
  ],
  "CytoTox­One™" : [
    iri : "${npoNS}NPO_1709"
  ],
  "LDH" : [
    iri : "${npoNS}NPO_1709"
  ],
  "Live/Dead" : [
    iri : "${baoNS}BAO_00030099" // general cell viability assay
  ],
  "MTS" : [
    iri : "${baoNS}BAO_0002456"
  ],
  "MTT" : [
    iri : "${npoNS}NPO_1911"
  ],
    "Modified MTT assay (MTT­formazan ppt dissolving by ethanol)" : [
      iri : "${npoNS}NPO_1911"
    ],
    "Modified MTT assay (MTT­formazan ppt dissolving by isopropanol/HCl)" : [
      iri : "${npoNS}NPO_1911"
    ],
  "NR" : [ // neutral red
    iri : "${baoNS}BAO_00030099" // general cell viability assay
  ],
  "Vialight" : [
    iri : "${baoNS}BAO_0003083" // FIXME: really a kit
  ],
  "WST­1" : [
    iri : "${baoNS}BAO_0140018" // FIXME: really a kit
  ],
  "XTT" : [
    iri : "${baoNS}BAO_0002458" // FIXME: really a kit
  ],
]

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
  "Bi" : [
    iri : "http://purl.enanomapper.org/onto/ENM_9000247",
    core : [ label : "bismuth", smiles : "[Bi]" ]
  ],
  "Carbon Nanotubes" : [
    iri : "http://purl.bioontology.org/ontology/npo#NPO_943",
    core : [ label : "carbon", smiles : "[C]" ]
  ],
  "Carbon NP" : [
    iri : "http://purl.obolibrary.org/obo/CHEBI_133602",
    core : [ label : "C", smiles : "C" ]
  ],
  "CdO" : [
    iri : "http://purl.enanomapper.org/onto/ENM_9000250",
    core : [ label : "CdO", smiles : "[Cd]=O" ]
  ],
  "CeO2" : [
    iri : "http://purl.enanomapper.org/onto/ENM_9000006",
    core : [ label : "CeO2", smiles : "O=[Ce]=O" ]
  ],
  "Chitosan" : [
    iri : "http://purl.bioontology.org/ontology/npo#NPO_261"
  ],
  "Co" : [
    iri : "http://purl.enanomapper.org/onto/ENM_9000248",
    core : [ label : "Co", smiles : "Co" ]
  ],
  "Co3O4" : [
    iri : "http://purl.enanomapper.org/onto/ENM_9000254"
  ],
  "Cr" : [
    iri : "http://purl.enanomapper.org/onto/ENM_9000249",
    core : [ label : "Cr", smiles : "Cr" ]
  ],
  "CuO" : [
    iri : "http://purl.bioontology.org/ontology/npo#NPO_1544",
    core : [ label : "CuO", smiles : "[Cu]=O" ]
  ],
  "CuS" : [
    iri : "http://purl.enanomapper.org/onto/ENM_9000246",
    core : [ label : "CuS", smiles : "[Cu]=S" ]
  ],
  "Cu2O" : [
    iri : "http://purl.obolibrary.org/obo/CHEBI_134402",
    core : [ label : "Cu2O", smiles : "[Cu]O[Cu]" ]
  ],
  "Dendrimer" : [
    iri : "http://purl.bioontology.org/ontology/npo#NPO_279"
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
  "MgO" : [
    iri: "http://purl.enanomapper.org/onto/ENM_9000252",
    core : [ label : "MgO", smiles : "[Mg]=O" ]
  ],
  "MnO" : [
    iri: "http://purl.enanomapper.org/onto/ENM_9000251",
    core : [ label : "MnO", smiles : "[Mn]=O" ]
  ],
  "Mo": [
    iri: "http://purl.enanomapper.org/onto/ENM_9000253",
    core : [ label : "Mo", smiles : "[Mo]" ]
  ],
  "PLGA" : [
    iri : "http://purl.enanomapper.org/onto/ENM_9000249",
    core : [ label : "γ-poly(L-glutamic acid) polymer", smiles : "C(CC(=O)O)[C@@H](C(=O)O)N" ]
  ],
  "Polystyrene" : [
    iri: "http://purl.enanomapper.org/onto/ENM_9000008"
  ],
  "Pt" : [
    iri: "http://purl.obolibrary.org/obo/CHEBI_50831",
    core : [ label : "Pt", smiles : "[Pt]" ]
  ],
  "QDs" : [
    iri : "http://purl.bioontology.org/ontology/npo#NPO_589",
  ],
  "Se" : [
    iri : "http://purl.enanomapper.org/onto/ENM_9000244",
    core : [ label : "Se", smiles : "[Se]" ]
  ],
  "SiO2" : [
    iri : "http://purl.bioontology.org/ontology/npo#NPO_1373",
    core : [ label : "SiO2", smiles : "O=[Si]=O" ]
  ],
  "Ti" : [
    iri : "http://purl.enanomapper.org/onto/ENM_9000245",
    core : [ label : "Ti", smiles : "[Ti]" ]
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
  "3-mercaptopropionic" : [
    label : "3-mercaptopropionic acid",
    smiles : "OC(=O)CCS"
  ],
  "alginate" : [
    label : "alginic acid"
  ],
  "BSA" : [
    label : "BSA"
  ],
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
  "Chitosan" : [
    label : "chitosan"
  ],
  "Citrate" : [
    label : "citrate",
    smiles : "C(C(=O)O)C(CC(=O)O)(C(=O)O)O"
  ],
  "cysteamine" : [
    label : "cysteamine",
    smiles : "SCCN"
  ],
  "D-penicillamin" : [
    label : "D-penicillamin",
    smiles : "CC(C)(S)[C@@H](N)C(O)=O"
  ],
  "Dextran" : [
    label : "dextran"
  ],
  "dimercaptosuccinic" : [
    label : "dimercaptosuccinic acid",
    smiles : "OC(=O)[C@@H](S)[C@@H](S)C(O)=O"
  ],
  "folic_acid" : [
    label : "folic acid",
    smiles : "Nc1nc2ncc(CNc3ccc(cc3)C(=O)N[C@@H](CCC(O)=O)C(O)=O)nc2c(=O)[nH]1"
  ],
  "hyaluronic_acid" : [
    label : "hyaluronic acid"
  ],
  "MeOH" : [
    label : "Methanol",
    smiles : "CO"
  ],
  "NH2" : [
    label : "amine",
    smiles : "N"
  ],
  "PEG" : [
    label : "polyethylene glycol"
  ],
  "PEI" : [
    label : "polyetherimide"
  ],
  "PVP" : [
    label : "poly(vinylpyrrolidone)"
  ],
  "SiO2" : [
    label : "SiO2",
    smiles : "O=[Si]=O"
  ],
  "silica" : [
    label : "SiO2",
    smiles : "O=[Si]=O"
  ],
  "Zn" : [
    label : "Zn", smiles : "[Zn]"
  ]
]

datasetIRI = "${metaNS}dataset"
rdf.addObjectProperty(store, datasetIRI, rdfType, "${voidNS}Dataset")
rdf.addDataProperty(store, datasetIRI, "${dctNS}title", "Meta-Analysis RDF")
rdf.addDataProperty(store, datasetIRI, "${dctNS}description", "RDF version of the data from ACS Nano. 2019, 21:2, 1583-1594. doi:10.1021/ACSNANO.8B07562.")
rdf.addDataProperty(store, datasetIRI, "${dctNS}publisher", "Egon Willighagen (H2020 NanoSolveIT)")
rdf.addObjectProperty(store, datasetIRI, "${dctNS}license", "https://creativecommons.org/publicdomain/zero/1.0/")

prevCellLine = null
prevTest = null
prevMetric = null

assayCount = 0;
toxCount = 0;
citations = 0;
articleCounter = 0
materialCounter = 0
data = excel.getSheet(inputFilename, 0, true)
for (i in 1..data.rowCount) {
  newMaterial = false
  newAssay = false
  
  // the nanomaterial
  name = data.get(i, "Nanoparticle")
  coating = data.get(i, "coat")
  if (coating != null) coating = coating.trim()

  // the diameter
  diameter = data.get(i, "Diameter (nm)")
  // the zeta potential
  zp = data.get(i, "Zeta potential (mV)")

  // literature
  artTitle = data.get(i, "Reference DOI")

  // unique id
  if (data.get(i, "Particle ID") == "") continue
  particleID = Double.valueOf(data.get(i, "Particle ID")).intValue()
  uniqueKey = "" + particleID + "_" + name + "_" + coating + "_" + diameter.trim()

  if (materials.containsKey(uniqueKey)) {
    enmIRI = materials.get(uniqueKey)
  } else {
    newMaterial = true;
    materialCounter++
    enmIRI = "${metaNS}m$materialCounter"
    materials.put(uniqueKey, enmIRI);
  }
  
  doi = artTitle.trim()
  if (!articles.contains(doi)) {
    if (doi.startsWith("10.")) {
      articleCounter++
      articles.add(doi)
      artIRI = "${metaNS}ref$articleCounter"
      rdf.addObjectProperty(store, artIRI, rdfType, "${biboNS}Article")
      rdf.addDataProperty(store, artIRI, "${dcNS}title" , doi)
      rdf.addObjectProperty(store, artIRI, "${owlNS}sameAs" , "https://doi.org/${doi}")
    }
  }

  if (newMaterial) {
    if (nanomaterials[name]) {
      // println "enm ${uniqueKey} / ${particleID}"
      newAssay = true
      prevCellLine = null
      prevTest = null
      prevMetric = null

      // the next material
      rdf.addObjectProperty(store, enmIRI, rdfType, chebi59999)
      rdf.addObjectProperty(store, enmIRI, "${dctNS}source" , datasetIRI)
      rdf.addDataProperty(store, enmIRI, rdfsLabel, name)

      // the components (they all have a core)
      coreIRI = "${enmIRI}_core"
      rdf.addObjectProperty(store, enmIRI, "${npoNS}has_part", coreIRI)
      rdf.addObjectProperty(store, coreIRI, rdfType, "${npoNS}NPO_1597")
      rdf.addDataProperty(store, coreIRI, rdfsLabel, name)

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
        origCoating = coating
        if (coating == "PEG to the PEI") coating = "PEG PEI"
        if (coating == "PEG-NH2") coating = "PEG NH2"
        if (coating == "PEG-OCH3") coating = "PEG MeOH"
        if (coating == "PEG-COOH") coating = "PEG COOH"
        if (coating == "folic acid with intermediate inorganic (silica) coating") coating = "folic_acid SiO2"
        if (coating == "folic acid with intermediate organic (PEG) coating") coating = "folic_acid PEG"
        if (coating == "Folic acid") coating = "folic_acid"
        if (coating == "D-penicillamine (NH2/COOH)") coating = "D-penicillamin"
        if (coating == "Zn then cysteamine") coating = "Zn cysteamine"
        if (coating == "Hyaluronic acid") coating = "hyaluronic_acid"
        if (coating == "3-mercaptopropionic acid (COOH)") coating = "3-mercaptopropionic"
        if (coating == "Citrate and PVP") coating = "Citrate PVP"

        coatingComponents = coating.split(" ")
        coatingCounter = 0
        for (component in coatingComponents) {
          if (coatings[component]) {
            coatingCounter++
            coatingIRI = "${enmIRI}_coating${coatingCounter}"
            rdf.addObjectProperty(store, enmIRI, "${npoNS}has_part", coatingIRI)
            rdf.addObjectProperty(store, coatingIRI, rdfType, "${npoNS}NPO_1367") // NPO_1367 = coating
            rdf.addDataProperty(store, coatingIRI, rdfsLabel, component)
            rdf.addObjectProperty(store, coatingIRI, "${ssoNS}CHEMINF_000200", smilesIRI)
            rdf.addObjectProperty(store, smilesIRI, rdfType, "${ssoNS}CHEMINF_000018")
            rdf.addDataProperty(store, smilesIRI, rdfsLabel, coatings[component].label)
            if (coatings[component].smiles) {
            smilesIRI = "${coatingIRI}_smiles"
              rdf.addDataProperty(store, smilesIRI, "${ssoNS}SIO_000300", coatings[component].smiles)
            }
          } else {
            logMessages += "Unrecognized coating component: $component\n"
          }
        }
        if (coatingCounter == 0) { // nothing recognized; add coating as separate coating
          coatingCounter++
          coatingIRI = "${enmIRI}_coating${coatingCounter}"
          rdf.addObjectProperty(store, enmIRI, "${npoNS}has_part", coatingIRI)
          smilesIRI = "${coatingIRI}_smiles"
          rdf.addObjectProperty(store, coatingIRI, rdfType, "${npoNS}NPO_1367") // NPO_1367 = coating
          rdf.addDataProperty(store, coatingIRI, rdfsLabel, origCoating)
          rdf.addObjectProperty(store, coatingIRI, "${ssoNS}CHEMINF_000200", smilesIRI)
          rdf.addObjectProperty(store, smilesIRI, rdfType, "${ssoNS}CHEMINF_000018")
          rdf.addDataProperty(store, smilesIRI, rdfsLabel, coating)
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
        rdf.addObjectProperty(store, assayIRI, rdfType, "${baoNS}BAO_0000015") // BAO_0000015 = bioassay
        rdf.addDataProperty(store, assayIRI, "${dcNS}title", "Diameter Assay")
        rdf.addObjectProperty(store, assayIRI, "${baoNS}BAO_0000209", measurementGroupIRI)
        rdf.addObjectProperty(store, assayIRI, "${dctNS}source", artIRI)
        rdf.addObjectProperty(store, enmIRI, "${oboNS}BFO_0000056", measurementGroupIRI)

        // the measurement group
        rdf.addObjectProperty(store, measurementGroupIRI, rdfType, "${baoNS}BAO_0000040")
        rdf.addObjectProperty(store, measurementGroupIRI, "${oboNS}OBI_0000299", endpointIRI)

        // the endpoint
        rdf.addObjectProperty(store, endpointIRI, rdfType, "${npoNS}NPO_1539")
        rdf.addObjectProperty(store, endpointIRI, rdfType, "${baoNS}BAO_0000179")
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
        rdf.addObjectProperty(store, endpointIRI, rdfType, "${baoNS}BAO_0000179")
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

  // now create triples for tox measurements
  if (nanomaterials[name]) {
    cellLine = data.get(i, "Cells")
    if (cellLine != prevCellLine) { prevCellLine = cellLine; newAssay = true }
    test = data.get(i, "Test").trim()
    if (test != prevTest) { prevTest = test; newAssay = true }
    metric = data.get(i, "Biochemical metric")
    if (metric != prevMetric) { prevMetric = metric; newAssay = true }

    concentration = data.get(i, "Concentration μM")
    viability = data.get(i, "% Cell viability")
    exposure = data.get(i, "Exposure time (h)")
    species1 = data.get(i, "Human(H)/Animal(A) cells")
    species2 = data.get(i, "Animal?")
    species = (species1 == "H" ? "Human" : (species2 != null ? species2.trim() : ""))

    if (newAssay) {
      endpointCounter = 0
      println "New assay: #${materialCounter} ${name}. ${cellLine}, ${exposure}, ${test}, ${metric}"
      
      assayCount++
      assayIRI = "${enmIRI}_toxAssay" + assayCount
      measurementGroupIRI = "${enmIRI}_toxMeasurementGroup" + assayCount

      toxCount++

      // the assay
      assayType = "${baoNS}BAO_0003009"
      if (bioassays[test]) {
        assayType = bioassays[test].iri
      } else {
        logMessages += "Unrecognized bioassay type: $test\n"
      }
      rdf.addObjectProperty(store, assayIRI, rdfType, "${assayType}")
      assayTitle = "${test} (${cellLine}, ${species})"
      rdf.addDataProperty(store, assayIRI, "${dcNS}title", assayTitle)
      rdf.addObjectProperty(store, assayIRI, "${baoNS}BAO_0000209", measurementGroupIRI)
      rdf.addObjectProperty(store, assayIRI, "${dctNS}source", artIRI)
      rdf.addObjectProperty(store, enmIRI, "${oboNS}BFO_0000056", measurementGroupIRI)
  
      // the measurement group
      rdf.addObjectProperty(store, measurementGroupIRI, rdfType, "${baoNS}BAO_0000040")
    }
    
    // the endpoint
    endpointCounter++
    endpointIRI = "${measurementGroupIRI}_toxEndpoint${endpointCounter}"
    rdf.addObjectProperty(store, measurementGroupIRI, "${oboNS}OBI_0000299", endpointIRI)

    rdf.addObjectProperty(store, endpointIRI, rdfType, "${npoNS}NPO_1816")
    rdf.addObjectProperty(store, endpointIRI, rdfType, "${baoNS}BAO_0000179")
    rdf.addObjectProperty(store, endpointIRI, "${oboNS}IAO_0000136", enmIRI)
    rdf.addDataProperty(store, endpointIRI, rdfsLabel, "Cell viability")
    rdf.addTypedDataProperty(store, endpointIRI, "${ssoNS}has-value", viability, "${xsdNS}double")
    rdf.addDataProperty(store, endpointIRI, "${ssoNS}has-unit", "%")

  }

}

if (ui.fileExists(logFilename)) ui.remove(logFilename)
logfile = ui.newFile(logFilename, logMessages )

if (ui.fileExists(outputFilename)) ui.remove(outputFilename)
output = ui.newFile(outputFilename, rdf.asTurtle(store) )

println "Materials: ${materials.size()}"
println "Assays: $assayCount"
println "  of which TOX: $toxCount"
println "Citations: $articleCounter"


