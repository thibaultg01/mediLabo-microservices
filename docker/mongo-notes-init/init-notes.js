db = db.getSiblingDB("notesdb");

db.notes.insertMany([
  {
    patId: 1,
    patient: "Test TestNone",
    note: "Le patient déclare qu'il 'se sent très bien' Poids égal ou inférieur au poids recommandé",
    createdAt: new Date(),
  },
  {
    patId: 2,
    patient: "Test TestBorderline",
    note: "Le patient déclare qu'il ressent beaucoup de stress au travail Il se plaint également que son audition est anormale dernièrement",
    createdAt: new Date(),
  },
  {
    patId: 2,
    patient: "Test TestBorderline",
    note: "Le patient déclare avoir fait une réaction aux médicaments au cours des 3 derniers mois Il remarque également que son audition continue d'être anormale",
    createdAt: new Date(),
  },
  {
    patId: 3,
    patient: "Test TestInDanger",
    note: "Le patient déclare qu'il fume depuis peu",
    createdAt: new Date(),
  },
  {
    patId: 3,
    patient: "Test TestInDanger",
    note: "Le patient déclare qu'il est fumeur et qu'il a cessé de fumer l'année dernière Il se plaint également de crises d’apnée respiratoire anormales Tests de laboratoire indiquant un taux de cholestérol LDL élevé",
    createdAt: new Date(),
  },
  {
    patId: 3,
    patient: "Test TestInDanger",
    note: "Le patient a signalé des vertiges et une fatigue inhabituelle.",
    createdAt: new Date(),
  },
  {
    patId: 4,
    patient: "Test TestEarlyOnset",
    note: "Le patient déclare qu'il lui est devenu difficile de monter les escaliers Il se plaint également d’être essoufflé Tests de laboratoire indiquant que les anticorps sont élevés Réaction aux médicaments",
    createdAt: new Date(),
  },
  {
    patId: 4,
    patient: "Test TestEarlyOnset",
    note: "Le patient déclare qu'il a mal au dos lorsqu'il reste assis pendant longtemps",
    createdAt: new Date(),
  },
  {
    patId: 4,
    patient: "Test TestEarlyOnset",
    note: "Le patient déclare avoir commencé à fumer depuis peu Hémoglobine A1C supérieure au niveau recommandé",
    createdAt: new Date(),
  },
  {
    patId: 4,
    patient: "Test TestEarlyOnset",
    note: "Taille, Poids, Cholestérol, Vertige et Réaction",
    createdAt: new Date(),
  },
]);
