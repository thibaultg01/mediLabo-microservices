# tests/test_api.py
from fastapi.testclient import TestClient
from app.main import app

client = TestClient(app)


def test_health():
    r = client.get("/health")
    assert r.status_code == 200
    assert r.json()["status"] == "UP"


def test_extract_and_assess():
    notes = [
        {"id": "n1", "text": "Le patient déclare qu'il fume depuis peu."},
        {"id": "n2", "text": "Hémoglobine A1C supérieure, LDL élevé."},
        {"id": "n3", "text": "Il ne fume pas et sa taille est normale."},
    ]

    r1 = client.post("/nlp/extract-triggers", json={"language": "fr", "notes": notes})
    assert r1.status_code == 200
    data = r1.json()
    assert data["totalTriggers"] >= 2

    r2 = client.post(
        "/assess",
        json={
            "patientId": 3,
            "sex": "M",
            "age": 21,
            "notes": notes,
        },
    )
    assert r2.status_code == 200
    out = r2.json()
    assert out["patientId"] == 3
    assert out["risk"] in ["None", "In Danger", "Early onset", "Borderline"]
