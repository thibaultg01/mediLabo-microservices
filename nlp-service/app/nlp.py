from typing import Dict, List, Tuple
import re
from .mapping import CANONICAL_TRIGGERS, TRIGGER_PATTERNS
from .utils import normalize, icase, is_negated

try:
    import spacy

    _SPACY_OK = True
    try:
        _NLP = spacy.load("fr_core_news_md")
    except Exception:
        _NLP = None
        _SPACY_OK = False
except Exception:
    _SPACY_OK = False
    _NLP = None


def extract_triggers(
    notes: List[Dict[str, str]],
) -> Tuple[List[Dict[str, object]], Dict[str, int], Dict[str, str]]:
    """
    matches: [{noteId, triggers: [..]}]
    counter: {canonical: count}
    """
    results: List[Dict[str, object]] = []
    counter: Dict[str, int] = {k: 0 for k in CANONICAL_TRIGGERS}
    explanations: Dict[str, str] = {}

    for item in notes:
        nid = item["id"]
        raw = item["text"] or ""
        text = normalize(raw)
        found: List[str] = []

        if _SPACY_OK and _NLP is not None:
            doc = _NLP(raw)
            lemmas = " ".join([t.lemma_.lower() for t in doc])
            norm_lemmas = normalize(lemmas)
            searchable = text + "\n" + norm_lemmas
        else:
            searchable = text

        for canonical, patterns in TRIGGER_PATTERNS.items():
            if canonical in found:
                continue
            for pat in patterns:
                for m in icase(pat).finditer(searchable):
                    if is_negated(searchable, m.start(), m.end()):
                        explanations[canonical] = (
                            f"Ignoré (négation détectée) via pattern '{pat}'"
                        )
                        break
                    found.append(canonical)
                    counter[canonical] += 1
                    explanations.setdefault(canonical, f"Match via pattern '{pat}'")
                    break
                if canonical in found:
                    break

        results.append({"noteId": nid, "triggers": found})

    total = sum(counter.values())
    explanations = {k: v for k, v in explanations.items() if counter.get(k, 0) > 0}
    return results, counter, explanations
