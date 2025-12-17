import re
from typing import Tuple
from unidecode import unidecode


def icase(pattern: str) -> re.Pattern:
    return re.compile(pattern, re.IGNORECASE | re.DOTALL)


# Normalisation : minuscules + suppression des accents
def normalize(text: str) -> str:
    return unidecode(text).lower()


def is_negated(text: str, start: int, end: int, window_words: int = 6) -> bool:
    left = max(0, start - 200)
    right = min(len(text), end + 200)
    window = text[left:right]

    NEGATIONS = [
        r"\bne\b[\s\w,'-]{0,20}\bpas\b",
        r"\bpas\s+de\b",
        r"\baucun(e)?\b",
        r"\bsans\b",
    ]

    return any(icase(p).search(window) for p in NEGATIONS)
