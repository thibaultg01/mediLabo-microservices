
from typing import Dict, List


# Canonicals attendus par les règles
CANONICAL_TRIGGERS: List[str] = [
"Hémoglobine A1C",
"Microalbumine",
"Taille",
"Poids",
"Fumeur",
"Anormal",
"Cholestérol",
"Vertiges",
"Rechute",
"Réaction",
"Anticorps",
]


TRIGGER_PATTERNS: Dict[str, List[str]] = {
"Hémoglobine A1C": [r"hemoglobine\s*a\s*1\s*c", r"hba1c", r"a1c"],
"Microalbumine": [r"micro\s*albumin(e|urie)?"],
"Taille": [r"\btaille\b", r"\bhauteur\b"],
"Poids": [r"\bpoids\b", r"\bimc\b", r"\bindice\s*de\s*m[ao]sse\s*corp\w*"],
"Fumeur": [r"\bfum(e|eur|euse|er)\w*", r"tabag(ique|isme)"],
"Anormal": [r"anormal(e|es|aux)?", r"\banorm\w+"],
"Cholestérol": [r"cholester(o|ô)l", r"\bldl\b", r"\bhdl\b", r"triglycerid(e|es)"],
"Vertiges": [r"vertig(e|es|eux)\w*", r"dizzy"],
"Rechute": [r"rechut(e|es|er)\w*", r"recidiv(e|es|er)\w*"],
"Réaction": [r"reaction\w*", r"allerg(ie|ique)\w*"],
"Anticorps": [r"anticorp(s)?", r"\bab\b", r"\bigg\b"],
}


NEGATIONS = [
r"ne\s+.*?\s+pas",
r"pas\s+de",
r"aucun(e)?\s+",
r"sans\s+",
r"n'\s*",
]