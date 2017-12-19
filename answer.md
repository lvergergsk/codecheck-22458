# answer.md

## 研究内容
#### 学部生の時の卒業論文
大学時代の研究内容は時系列の雑音フィルタリングについてでした。
その中で特にExtended Kalman Filter(EKF)とParticle Filter(PF)についての原理、実装、そして性能の比較について着手しました。
  - EKFとPFは両方ともHidden Markov Modelを使用しています。EKFは与えられたモデルに基づきアナリティカル的に非線形モデルの次のタイムステップの分布(Mean&Variance)を予測します、しかしEKFの使用は多くの場合正規分布を基づくデータに限られています。
PFはSequential Monte Carloというシミュレーション方法を使い、次のタイムステップのデータをモデルから与えられた条件付き分布から生成して、その分布を予測する方法です。
モデルの分布に制限がないが、いい予測を得るためにより多くの乱数を生成必要な計算資源が多く、そして正規分布の場合のフィルタリング効果はEKFよりおとる。シミュレーションはC++で実装しました。

#### 修士課程
修士課程は実践寄りのコースを選びましたので卒業研究はありません。卒業研究をする学生より多くコースとる必要があります、現時点でGPAは3.5。コースのリスティング:

| Title                                                                               | Description                                                                                                                   |
|:------------------------------------------------------------------------------------|:------------------------------------------------------------------------------------------------------------------------------|
| Programming Language Principle (プログラミング言語)                                 | プログラミング言語の正規モデル, 設計目的, run-time構造, 言語の実装など。                                                      |
| Analysis of Algorithms　(アルゴリズム解析)                                          | アルゴリズムの設計、複雑度。実際のアルゴリズムを学習、新しいアルゴリズムを実際に作る。                                        |
| Math for Intelligent Systems (数学)                                                 | トポロジー、線形代数、確率、テンソル、フーリエ変換、σ-集合代数など。                                                          |
| Machine Learning　(機械学習)                                                        | 決定木、リスク関数、不等式と理論の証明、SVM、最急降下法、NN、Convolutional NN, Recurrent NNなど。                             |
| Penetration Testing and Ethical Hacking　(ハッキング、ネットワークセキュリティキー) | ネットワーク、パスワードクラッキング、ウェブアプリケーションへのスクリプトインジェクションなど。                              |
| Software Engineering　(ソフトウェア工学)                                            | ソフトウェア開発に関する方法、ツール、コスト効率、プロジェクトマネジメントなど。                                              |
| Computer Architecture Principles (電脳構造)                                         | CPU、キャッシュ、メモリ、ハードディスクの仕組みと高速化、そしてコンパイラーの役割とコンパイラーに友好的なコードの書き方など。 |
| Computer Vision (電脳視覚)                                                          | 画像処理に必要な線形代数、DLT、輪郭認識、ベジェ曲線な。                                                                       |
| Advanced Data Structure (データ構造)                                                | 償却解析、木構造、ヒープ構 (e.g. AVL木、フィボナッチ木、二分木、B木、B+木、トライ木、接尾辞木など)                            |

## 開発物・成果物
- Extended Kalman Filter & Particle Filter
  - 内容：モデルによって作られた時系列元データにホワイトノイズにかけた
ものを入力とし、それを元データに還元するプログラム。QtでGUIとプロッティングを作りました。
  - 使用した技術：C++、Qt。


- データ構造とアルゴリズム C++
  - 内容:　主にHackerRankとAIZUを利用しています。例を挙げるとヒープ構造（オーグメントを加えたものを含む）やソーティング、フィボナッチヒープ、素集合データ構造、動的計画法、貪欲法、ダイクストラ法（最短経路）などがあります。
  - 使用した技術：JAVA、C++、Python、JUnit。


- 機械学習、ニュロラルネットワーク
  - 内容：全接続、Feed-Forwardのニュロラルネットワークの実装。Xorを正確に識別できる。MFCCで単語の音声データを入力に変換して分類させることができます。
  - 使用した技術：C++。


- コンパイラー JAVA
  - 内容：LL1 プログラミング言語をパーシングして木構造（Abstract Syntax Tree (AST) と Concrete Syntax Tree (CST)）に変換し、Java bytecode を生成して、JVM を使ってネイティブコードに変換。
  - 使用した技術：JAVA.


- 投票統計システム（ユーザ認証とセッション管理の部分）
  - フロントエンド： Angular4 でユーザーインターフェースを作り、REST で
サーバーにリクエストを送る。
  - バックエンド：Golang でルーターを作りリクエストに対し返答を送る。
セッションは Json Web Token(JWT)を使用。投票のデータはサーバー側の
MySQL データベースに保存、データをアクセスして分析する。
  - テスト：Karma, Jasmine と Postman を使用、ユニットテスト、インテグ
レーションテスト、API テスト、E2E テストを行いました。
  - 使用した技術：Angular 4、TypeScript、SQL、Golang、JWT、REST、Karma、Jasmine、Postman、Git。