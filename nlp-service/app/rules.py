from typing import Literal

RISK = Literal["None", "Borderline", "In Danger", "Early onset"]
RULES_VERSION = "1.0"


def assess_risk(age: int, sex: Literal["M", "F"], trigger_count: int) -> RISK:
    if trigger_count == 0:
        return "None"

    if age > 30 and 2 <= trigger_count <= 5:
        return "Borderline"

    if age < 30:
        if sex == "M":
            if trigger_count >= 5:
                return "Early onset"
        if trigger_count >= 3:
            return "In Danger"
        else:
            if trigger_count >= 7:
                return "Early onset"

    if trigger_count >= 4:
        return "In Danger"
    else:
        if trigger_count >= 8:
            return "Early onset"
        if 6 <= trigger_count <= 7:
            return "In Danger"

    return "None"


def explain_rules(age: int, sex: str, trigger_count: int, risk: RISK) -> str:
    if risk == "None":
        return f"{trigger_count} déclencheur(s) ⇒ Aucun risque identifié."

    if risk == "Borderline":
        return f"Âge > 30 et {trigger_count} déclencheurs ⇒ Borderline."

    if risk == "In Danger":
        if age < 30:
            seuil = 3 if sex == "M" else 4
            return f"<30 ans ({sex}), {trigger_count} déclencheurs ≥ {seuil} ⇒ In Danger."
        return f">30 ans et 6–7 déclencheurs ⇒ In Danger."

    if risk == "Early onset":
        if age < 30:
            seuil = 5 if sex == "M" else 7
            return f"<30 ans ({sex}), {trigger_count} déclencheurs ≥ {seuil} ⇒ Early onset."
        return f">30 ans et ≥ 8 déclencheurs ⇒ Early onset."

    return ""
