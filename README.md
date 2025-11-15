# Symfony Log Viewer

A web-based interface for viewing, filtering, and analyzing Symfony application logs (incl. Messenger logs). This tool provides a convenient way to browse and debug your Symfony application without having to manually parse log files or opening them in an editor.

## Features

- **Log Browsing**: View Symfony logs in a user-friendly web interface
- **Filtering**: Filter logs by log level, error type, and other criteria for easier debugging
- **Offline**: The log viewer can be fully used offline, locally within your web browser (its just HTML, JS and CSS)
- **Conveniance**: No need to manually go through the log lines in a file editor or in the terminal

_TODOs:_

- **Search**: Search through logs to quickly find relevant entries
- **Advanced Filtering:** More advanced filter capabilities 
- **Good looking:** Improved design, which is both appealing to your eyes as well as functional at the same time

## Getting started

### Prerequisites

- Java (17 or higher), see: `java -version`

### Start web app

Our project comes with the Gradle Wrapper, which you can use to setup and start the web app.

Under Linux (or MacOS), run:

```sh
./gradlew run
```

Once the project has started successfully, open your browser and go to [http://localhost:3000](http://localhost:3000).

_Note #1:_ Use the `-t` or the `--continuous` parameter on the `run` task to automatically hot-reload the project whenever a change is detected (ideal during development).

_Note #2:_ If no task is provided, the default task is just running the `build` task.

To view all the available tasks, run the following command:

```sh
./gradlew tasks
```

---

For Windows, run:

```sh
gradlew.bat run
```