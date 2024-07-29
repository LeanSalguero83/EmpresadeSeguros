docker compose -f docker/docker-compose.yml down --remove-orphans
docker rmi best_insurance/api
gradlew build && gradlew bootBuildImage && docker compose -f docker/docker-compose.yml up