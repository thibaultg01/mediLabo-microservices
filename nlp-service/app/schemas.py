from pydantic import BaseModel, Field
from typing import List, Dict, Optional, Literal


class NoteIn(BaseModel):
    id: str = Field(..., description="ID unique de la note")
    text: str = Field(..., description="Contenu brut de la note")


class AnalyzeRequest(BaseModel):
    language: Literal["fr", "en"] = "fr"
    notes: List[NoteIn]


class AnalyzeResponse(BaseModel):
    matches: List[Dict[str, object]]
    triggerCounts: Dict[str, int]
    totalTriggers: int
    explanations: Dict[str, str]


class AssessmentRequest(BaseModel):
    patientId: int
    sex: Literal["M", "F"]
    age: int
    notes: List[NoteIn]


class AssessmentResponse(BaseModel):
    patientId: int
    age: int
    sex: Literal["M", "F"]
    triggerCount: int
    triggers: List[str]
    risk: Literal["None", "Borderline", "In Danger", "Early onset"]
    rulesVersion: str
    details: str
