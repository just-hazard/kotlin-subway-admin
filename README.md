<p align="center">
    <img width="200px;" src="https://raw.githubusercontent.com/woowacourse/atdd-subway-admin-frontend/master/images/main_logo.png"/>
</p>
<p align="center">
  <img alt="npm" src="https://img.shields.io/badge/npm-%3E%3D%205.5.0-blue">
  <img alt="node" src="https://img.shields.io/badge/node-%3E%3D%209.3.0-blue">
  <a href="https://edu.nextstep.camp/c/R89PYi5H" alt="nextstep atdd">
    <img alt="Website" src="https://img.shields.io/website?url=https%3A%2F%2Fedu.nextstep.camp%2Fc%2FR89PYi5H">
  </a>
  <img alt="GitHub" src="https://img.shields.io/github/license/next-step/atdd-subway-admin">
</p>

<br>

# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

<br>

## 🚀 Getting Started

### Install
#### npm 설치
```
cd frontend
npm install
```
> `frontend` 디렉토리에서 수행해야 합니다.

### Usage
#### webpack server 구동
```
npm run dev
```
#### application 구동
```
./gradlew bootRun
```
<br>

## ✏️ Code Review Process
[텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

<br>

## 🐞 Bug Report

버그를 발견한다면, [Issues](https://github.com/next-step/atdd-subway-admin/issues) 에 등록해주세요 :)

<br>

## 📝 License

This project is [MIT](https://github.com/next-step/atdd-subway-admin/blob/master/LICENSE.md) licensed.

### 3단계 요구사항 정리
#### 구간 등록 API 구현
* 가능한 케이스
  * 역 사이에 새로운 역을 등록할 경우
    * 새로운 길이를 뺀 나머지를 새롭게 추가된 역과의 길이로 설정
  * 새로운 역을 상행 종점으로 등록
  * 새로운 역을 하행 종점으로 등록
  * 어떤 형태로 종점인지 역과 역사이 등록될 역인지 판별
* 예외 케이스
  * 역과 역 사이에 새로운 역 등록 시 기존 길이보다 크거나 같으면 예외
  * 상행역과 하행역이 이미 모두 등록되어 있다면 추가 불가
  * 둘다 없어도 추가 불가 (Cascade 전략 어떻게 가져갈지 고민)
  * 
