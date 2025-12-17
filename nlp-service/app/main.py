# app/main.py
from fastapi import FastAPI

from .schemas import (
    AnalyzeRequest,
    AnalyzeResponse,
    AssessmentRequest,
    AssessmentResponse,
)
from .nlp import extract_triggers
from .rules import assess_risk, explain_rules, RULES_VERSION

app = FastAPI(title="MediLabo – NLP Notes Médicales", version="1.0.0")

from prometheus_fastapi_instrumentator import Instrumentator

Instrumentator().instrument(app).expose(app, endpoint="/metrics")


@app.get("/health")
def health():
    return {"status": "UP"}


@app.post("/nlp/extract-triggers", response_model=AnalyzeResponse)
def api_extract(req: AnalyzeRequest):
    matches, counter, explanations = extract_triggers(
        [n.model_dump() for n in req.notes]
    )
    total = sum(counter.values())
    nonzero = {k: v for k, v in counter.items() if v > 0}
    return AnalyzeResponse(
        matches=matches,
        triggerCounts=nonzero,
        totalTriggers=total,
        explanations=explanations,
    )


@app.post("/assess", response_model=AssessmentResponse)
def api_assess(req: AssessmentRequest):
    matches, counter, _ = extract_triggers([n.model_dump() for n in req.notes])
    total = sum(counter.values())
    triggers_list = [k for k, v in counter.items() if v > 0]

    risk = assess_risk(req.age, req.sex, total)
    details = explain_rules(req.age, req.sex, total, risk)

    return AssessmentResponse(
        patientId=req.patientId,
        age=req.age,
        sex=req.sex,
        triggerCount=total,
        triggers=triggers_list,
        risk=risk,
        rulesVersion=RULES_VERSION,
        details=details,
    )
