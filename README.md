# hst - A simple hosts file manager

This application allows you to easily manage your hosts file.

## Installation

### Download an artifact from GitHub Actions

Download latest build from here.
[https://github.com/rokuosan/hst/actions/workflows/build.yaml](https://github.com/rokuosan/hst/actions/workflows/build.yaml)

If there are no artifacts, you have to build from source.

### Build from Source

####  1. Clone this repository

```shell
$ git clone https://github.com/rokuosan/hst
```

####  2. Build native image with Gradle Wrapper

> Requirements:
> - GraalVM 21 or higher

Run this command.

```shell
$ ./gradlew nativeCompile
```

After that, you can find executable native binary in ``./build/native/nativeCompile/``

## Flags

| Flag           | Type                        | Description |
|----------------|-----------------------------|-------------|
| -a, --address  | String(IP Address)          |             |
| -n, --hostname | String                      |             |
| --aliases      | String(separate with Space) |             |

### Example

To add new entry like ``192.168.10.100 server.local web.local``.

```shell
$ sudo hst add -a 192.168.10.100 -n server.local --aliases web.local
```