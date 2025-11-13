#!/usr/bin/env python3
"""Launch Job Quest Game via run.sh."""
import subprocess
import sys
from pathlib import Path


def main() -> int:
    script_dir = Path(__file__).resolve().parent
    run_sh = script_dir / "run.sh"

    if not run_sh.is_file():
        print(f"Error: {run_sh} not found.", file=sys.stderr)
        return 1

    # Forward any extra CLI args to run.sh
    cmd = ["bash", str(run_sh), *sys.argv[1:]]

    try:
        completed = subprocess.run(cmd, check=False)
        return completed.returncode
    except KeyboardInterrupt:
        print("\nInterrupted.", file=sys.stderr)
        return 130
    except FileNotFoundError:
        print("Error: bash not found.", file=sys.stderr)
        return 127
    except Exception as e:  # noqa: BLE001
        print(f"Error running run.sh: {e}", file=sys.stderr)
        return 1


if __name__ == "__main__":
    sys.exit(main())
