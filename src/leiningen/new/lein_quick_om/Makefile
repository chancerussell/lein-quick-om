PROJ_NAME := {{sanitized}}
CURR_DATE := $(shell date +%Y%m%d_%H%M%S)
CURR_BRANCH := $(shell git rev-parse --abbrev-ref HEAD)
PROJ_DIR := $(shell pwd)
SRC_DIR := $(PROJ_DIR)/src
RES_DIR := $(PROJ_DIR)/resources
M2_DIR := $(PROJ_DIR)/local-m2
DOCKER_TAG := $(PROJ_NAME):$(CURR_BRANCH)-$(CURR_DATE)
LATEST_TAG := $(PROJ_NAME):$(CURR_BRANCH)-latest
CONTAINER_PORT := 3449
HOST_PORT := 3449

dbuild:
	docker build -t $(DOCKER_TAG) .
	docker tag -f $(DOCKER_TAG) $(LATEST_TAG)

figwheel:
	- docker run \
		-it \
		-v $(SRC_DIR):/app/src \
		-v $(RES_DIR):/app/resources \
		-p $(CONTAINER_PORT):$(HOST_PORT) \
		$(LATEST_TAG) \
		lein figwheel

#include local-m2 directory to take advantage of cached deps
quick:
	- docker run \
		-it \
		-v $(SRC_DIR):/app/src \
		-v $(RES_DIR):/app/resources \
		-v $(M2_DIR):/app/local-m2 \
		-p $(CONTAINER_PORT):$(HOST_PORT) \
		$(LATEST_TAG) \
		lein figwheel

