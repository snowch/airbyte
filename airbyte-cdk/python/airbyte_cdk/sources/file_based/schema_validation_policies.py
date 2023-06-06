from enum import Enum
from typing import Any, Mapping


class UserValidationPolicies(Enum):
    SKIP_RECORD = "skip_record_on_schema_mismatch"
    EMIT_RECORD = "emit_record_on_schema_mismatch"
    WAIT = "wait_for_discover_on_schema_mismatch"


def record_passes_validation_policy(
    policy: str, record: Mapping[str, Any], schema: Mapping[str, Any]
):
    """
    Return True if the record passes the user's validation policy.
    """
    return True
