"""
Application Flask
"""

from flask import Flask, render_template
from conf import COMMIT_DATE, COMMIT_SHORT_HASH, COMMIT_AUTHOR, COMMIT_BRANCH

app = Flask(__name__)


@app.route("/")
def hello_world():
    """
    Fonction hello_world
    """
    # return "<p>Hello, World!</p>"
    message = "Hello world!"
    return render_template(
        "index.html",
        message=message,
        commit_date=COMMIT_DATE,
        commit_short_hash=COMMIT_SHORT_HASH,
        commit_author=COMMIT_AUTHOR,
        commit_branch=COMMIT_BRANCH,
    )
