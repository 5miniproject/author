# 

## Model
www.msaez.io/#/108072455/storming/Aivle_Library_18_miniProj

## Before Running Services
### Make sure there is a Kafka server running
```
cd kafka
docker-compose up
```
- Check the Kafka messages:
```
cd infra
docker-compose exec -it kafka /bin/bash
cd /bin
./kafka-console-consumer --bootstrap-server localhost:9092 --topic
```

## Run the backend micro-services
See the README.md files inside the each microservices directory:

- author
- subscribe
- point
- script
- library
- serviceai


## Run API Gateway (Spring Gateway)
```
cd gateway
mvn spring-boot:run
```

## Test by API
- author
```
 http :8088/authors id="id"email="email"name="name"detail="detail"portfolio="portfolio"isApprove="isApprove"
```
- subscribe
```
 http :8088/subscribers id="id"email="email"name="name"isPurchased="isPurchased"registerDate="registerDate"purchaseDate="PurchaseDate"notification="notification"isKt="isKT"
 http :8088/subscribeBooks id="id"subscriberId="subscriberId"authorId="authorId"bookId="bookId"isSubscribed="isSubscribed"status="status"subscriptionDate="subscriptionDate"subscriptionExpiredDate="subscriptionExpiredDate"title="title"
```
- point
```
 http :8088/points userId="userId"point="point"isKt="isKT"
```
- script
```
 http :8088/bookScripts id="id"authorId="authorId"contents="contents"status="status"createdAt="createdAt"updatedAt="updatedAt "title="title"authorname="authorname"
```
- library
```
 http :8088/books id="id"authorId="authorId"publicationId="publicationId"contents="contents"coverImageUrl="coverImageURL"plot="plot"views="views"status="status"category="category"subscriptionFee="subscriptionFee"plotUrl="plotURL"isBest="isBest"title="title"authorName="authorName"
```
- serviceai
```
 http :8088/publications id="id"authorId="authorId"scriptId="scriptId"title="title"contents="contents"coverImageUrl="coverImageURL"plot="plot"status="status"plotUrl="plotURL"category="category"subscriptionFee="subscriptionFee"authorname="authorname"
```


## Run the frontend
```
cd frontend
npm i
npm run serve
```

## Test by UI
Open a browser to localhost:8088

## Required Utilities

- httpie (alternative for curl / POSTMAN) and network utils
```
sudo apt-get update
sudo apt-get install net-tools
sudo apt install iputils-ping
pip install httpie
```

- kubernetes utilities (kubectl)
```
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl
```

- aws cli (aws)
```
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
unzip awscliv2.zip
sudo ./aws/install
```

- eksctl 
```
curl --silent --location "https://github.com/weaveworks/eksctl/releases/latest/download/eksctl_$(uname -s)_amd64.tar.gz" | tar xz -C /tmp
sudo mv /tmp/eksctl /usr/local/bin
```

### 테스트
gitpod commit