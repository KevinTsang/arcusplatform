## Building the Iris 2 docker images


Running the following command on the console will build all of the docker
images required for the Iris 2 platform.

```
$ ./bin/build.sh
```

Docker caches intermediate images to speed up image creation, so running this
command after making small changes will not take as long as the first run.

## Bringing up the Iris 2 docker container

Running the following command on the console will start a new docker container
named "eyeris" using the Iris 2 uber Docker image "eyeris/uber".

```
$ ./bin/start.sh
```

There are several environment variables that control the creation of the new
docker container. The most important ones are:

    * EYERIS_PLATFORM_NAME (default: eyeris)
    Setting this to a value other than "eyeris" will allow you to start up
    multiple Iris 2 uber images. These images, however, will not be clustered
    together. They will just be multiple independent stacks.

    * EYERIS_PLATFORM_CPUSHARES (default: 1)
    This value controls the number of cpu shares given to the Iris 2 uber
    container. The value only matters if multiple containers are running.

    * EYERIS_PLATFORM_MEMORY (default: 1g)
    This value controls the maximum amount of memory that can be allocated
    inside the container. The JVM heap sizes used inside the container are
    not currently configurable, however.

    * EYERIS_PLATFORM_DIRECT_PORTS (default: empty)
    Setting this value to anything other than the empty string will cause
    the container's ports to be mapped to the same ports on the host. This
    allows for connecting clients running directly on the host to the
    standard service ports. If this value is empty (the default) then
    docker will map the container's ports to a random host port.

## Bringing down an Iris 2 docker container

Running the following command on the console will stop an existing Iris 2 uber
container. This command also understands the EYERIS\_PLATFORM\_NAME environment
variable, allowing multiple containers to be started and stopped easily.

```
$ ./bin/stop.sh
```

## Connecting to a running Iris 2 docker container

Running the following command on the console will run a given executable inside
an existing Iris 2 uber container. This command also understands the
EYERIS\_PLATFORM\_NAME environment variable.

```
$ ./bin/connect.sh [command]
```

If a command is not given then the script will start a bash shell by default.


## Building Docker Containers

```
./gradlew :khakis:distDocker
```

## Pushing docker containers

./khakis/bin/tag.sh and ./khakis/bin/push.sh accept two environment variables which change the path of the image to tag or push.

REGISTRY_SEPERATOR controls the seperator between image names. e.g. eyeris/java or eyeris-java. In the case of Google Container Registry (gcr) this can be '/', but for DockerHub this will need to be '-'.

REGISTRY_NAME specifies where to push the container to, e.g. gcr.io/YOURPROJECT or your DockerHub account name.
`
First tag:
```
REGISTRY_SEPERATOR='/' REGISTRY_NAME=gcr.io/arcus-238802 ./khakis/bin/tag.sh
```
then push:
```
REGISTRY_SEPERATOR='/' REGISTRY_NAME=gcr.io/arcus-238802 ./khakis/bin/push.sh
```
