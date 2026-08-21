package com.example.demo.bookcontrol.bookcreate.service;

import java.io.ByteArrayInputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
// XML操作用クラスのインポート（W3C DOM標準API）
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.example.demo.bookcontrol.bookcreate.entity.Book;
import com.example.demo.bookcontrol.bookcreate.entity.BookDto;
import com.example.demo.bookcontrol.bookcreate.repository.BookRepository;

/**
 * 書籍管理に関するビジネスロジックを担当するサービス層クラス
 */
@Service
public class BookService {

	// データベース操作を行うリポジトリクラスを自動注入（DI）[cite: 15, 16]
	@Autowired
	private BookRepository bookRepository;

	// application.properties からプロキシ接続のホスト名を取得（未設定時は空文字）[cite: 16]
	@Value("${system.proxy.host:}")
	private String proxyHost;

	// application.properties からプロキシ接続のポート番号を取得（未設定時は 0）[cite: 16]
	@Value("${system.proxy.port:0}")
	private int proxyPort;

	/**
	 * 国立国会図書館(NDL)のAPIを利用して、ISBNから書籍情報を自動取得する
	 * 
	 * @param isbn 検索対象のISBN番号
	 * @return 取得した書籍情報を詰めたBookDtoオブジェクト
	 */
	public BookDto fetchBookInfoFromNdl(String isbn) {
		// 入力されたISBNからハイフンを除去し、前後の不要な空白を削除[cite: 16]
		String cleanIsbn = isbn.replaceAll("-", "").trim();

		// 国会図書館 OpenSearch API の検索用URLを作成[cite: 16]
		String url = "https://ndlsearch.ndl.go.jp/api/opensearch?isbn=" + cleanIsbn;

		// HTTP通信を行うためのファクトリ設定オブジェクトを作成[cite: 16]
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();

		// プロキシ設定が存在する場合のみ、HTTPプロキシを適用する（社内LAN等への対策）[cite: 16]
		if (proxyHost != null && !proxyHost.isEmpty() && proxyPort > 0) {
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
			requestFactory.setProxy(proxy);
		}

		// 通信のタイムアウト設定（接続・読み込みともに15秒でタイムアウト）[cite: 16]
		requestFactory.setConnectTimeout(30000);
		requestFactory.setReadTimeout(30000);

		// 設定を適用した RestTemplate（外部API呼び出し用クライアント）を生成[cite: 16]
		RestTemplate restTemplate = new RestTemplate(requestFactory);

		// 画面返却用のDTOオブジェクトを生成し、整形済みISBNを設定[cite: 16]
		BookDto bookDto = new BookDto();
		bookDto.setIsbn(cleanIsbn);

		try {
			// 国会図書館APIへリクエストを送信し、レスポンスのXMLを文字列として受け取る[cite: 16]
			String xmlResponse = restTemplate.getForObject(url, String.class);

			if (xmlResponse != null) {
				// XML解析用のビルダーファクトリを生成[cite: 16]
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();

				// XML文字列をバイト流に変換してパースを行い、DOMツリー構造（Document）を構築[cite: 16]
				Document doc = builder.parse(new ByteArrayInputStream(xmlResponse.getBytes(StandardCharsets.UTF_8)));

				// XMLの中から <item> タグの要素一覧を取得[cite: 16]
				NodeList items = doc.getElementsByTagName("item");

				// 検索結果の書籍データ（<item>）が存在する場合[cite: 16]
				if (items.getLength() > 0) {
					// 該当する最初の1件目の要素を取り出す[cite: 16]
					Element item = (Element) items.item(0);

					// --- 1. タイトルの取得 ---[cite: 16]
					NodeList titleList = item.getElementsByTagName("title");
					if (titleList.getLength() > 0) {
						bookDto.setTitle(titleList.item(0).getTextContent());
					}

					// --- 2. 著者の取得 (<author> または <dc:creator>) ---[cite: 16]
					NodeList authorList = item.getElementsByTagName("author");
					if (authorList.getLength() > 0) {
						bookDto.setAuthor(authorList.item(0).getTextContent());
					} else {
						// <author> がない場合は <dc:creator> タグを探す[cite: 16]
						NodeList creatorList = item.getElementsByTagName("dc:creator");
						if (creatorList.getLength() > 0) {
							bookDto.setAuthor(creatorList.item(0).getTextContent());
						}
					}

					// --- 3. 出版社の取得 (<dc:publisher>) ---[cite: 16]
					NodeList publisherList = item.getElementsByTagName("dc:publisher");
					if (publisherList.getLength() > 0) {
						bookDto.setPublisher(publisherList.item(0).getTextContent());
					}

					// --- 4. サムネイル画像のURL設定 ---[cite: 16]
					// 国会図書館APIで画像が取れないため、openBDの無料画像APIを利用して自動生成[cite: 16]
					bookDto.setThumbnailUrl("https://cover.openbd.jp/" + cleanIsbn + ".jpg");
				}
			}
		} catch (ResourceAccessException e) {
			// タイムアウトやネットワークエラーが発生した場合
			System.err.println("国会図書館APIへの接続がタイムアウトしました: " + e.getMessage());
		} catch (HttpClientErrorException.TooManyRequests e) {
			// 429 リクエスト過多の場合
			System.err.println("国会図書館APIのリクエスト上限に達しました: " + e.getMessage());
		} catch (Exception e) {
			// その他の予期せぬエラー
			e.printStackTrace();
		}
		return bookDto;
	}

	public void saveBook(BookDto bookDto) {
		// 【修正】new Books(...) から new Book(...) に変更
		Book book = new Book();
		book.setIsbn(bookDto.getIsbn());
		book.setTitle(bookDto.getTitle());
		book.setAuthor(bookDto.getAuthor());
		book.setPublisher(bookDto.getPublisher());
		book.setThumbnailUrl(bookDto.getThumbnailUrl());

		bookRepository.save(book);
	}

	public void saveAllBooks(List<BookDto> dtoList) {
		List<Book> books = dtoList.stream()
				.map(dto -> {
					Book book = new Book();
					book.setIsbn(dto.getIsbn());
					book.setTitle(dto.getTitle());
					book.setAuthor(dto.getAuthor());
					book.setPublisher(dto.getPublisher());
					book.setThumbnailUrl(dto.getThumbnailUrl());
					return book;
				})
				.toList();
		bookRepository.saveAll(books);
	}

	// 【追加】ISBNで検索するメソッド
	public BookDto findBookByIsbn(String isbn) {
		return bookRepository.findById(isbn)
				.map(entity -> {
					BookDto dto = new BookDto();
					dto.setIsbn(entity.getIsbn());
					dto.setTitle(entity.getTitle());
					dto.setAuthor(entity.getAuthor());
					dto.setPublisher(entity.getPublisher());
					dto.setThumbnailUrl(entity.getThumbnailUrl());
					return dto;
				})
				.orElse(null);
	}
}