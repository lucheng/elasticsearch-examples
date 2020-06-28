package org.cheng.elasticsearch;

import java.util.ArrayList;
import java.util.List;

import org.cheng.elasticsearch.dto.Student;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ScrolledPage;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

public class ESPatchTest extends ElasticsearchApplicationTest {

	@Autowired
	private ElasticsearchTemplate esTemplate;

	// 创建索引
	@Test
	public void createIndex() {
		for (int i = 0; i < 5; i++) {
			Student stu = new Student();
			stu.setStuId(1000L + i);
			stu.setName("Nick" + i);
			stu.setAge(20 + i);
			stu.setMoney(18.9f + i);
			stu.setSign("I am a teacher 中文测试");
			stu.setDescription("I am a english teacher 中文测试" + i);
			IndexQuery indexQuery = new IndexQueryBuilder().withObject(stu).build();
			esTemplate.index(indexQuery);
		}
	}

	@Test
	public void searchStuDoc() {
		Pageable pageable = PageRequest.of(1, 10);
		SearchQuery query = new NativeSearchQueryBuilder().withQuery(QueryBuilders.matchQuery("description", "中测"))
				.withPageable(pageable).build();
		AggregatedPage<Student> pageStu = esTemplate.queryForPage(query, Student.class);

		System.out.println("总分页数:" + pageStu.getTotalPages());
		List<Student> stuList = pageStu.getContent();
		for (Student student : stuList) {
			System.out.println(student);
		}
	}

	@Test
	public void highlightStuDoc() {
		String preTag = "<font color='red'>";
		String postTag = "</font>";
		Pageable pageable = PageRequest.of(0, 2);
		SearchQuery query = new NativeSearchQueryBuilder()
				.withQuery(QueryBuilders.matchQuery("description", "a teacher 中 测"))
				.withHighlightFields(new HighlightBuilder.Field("description").preTags(preTag).postTags(postTag))
				.withPageable(pageable).build();
		AggregatedPage<Student> pageStu = esTemplate.queryForPage(query, Student.class, new SearchResultMapper() {
			@Override
			public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
				List<Student> stuHighlight = new ArrayList<>();
				SearchHits hits = searchResponse.getHits();
				for (SearchHit h : hits) {
					HighlightField highlightField = h.getHighlightFields().get("description");
					Integer stuId = (Integer) h.getSourceAsMap().get("stuId");
					String name = (String) h.getSourceAsMap().get("name");
					Integer age = (Integer) h.getSourceAsMap().get("age");
					String sign = (String) h.getSourceAsMap().get("sign");
					Object money = h.getSourceAsMap().get("money");
					// 高亮数据
					String description = highlightField.getFragments()[0].toString();
					Student studentHL = new Student();
					studentHL.setDescription(description);
					studentHL.setStuId(Long.parseLong(stuId.toString()));
					studentHL.setName(name);
					studentHL.setAge(age);
					studentHL.setSign(sign);
					studentHL.setMoney(Float.parseFloat(money.toString()));
					stuHighlight.add(studentHL);
				}
				if (!stuHighlight.isEmpty()) {
					return new AggregatedPageImpl<>((List<T>) stuHighlight);
				}
				return null;
			}

			@Override
			public <T> T mapSearchHit(SearchHit searchHit, Class<T> aClass) {
				return null;
			}
		});

		System.out.println("总分页数:" + pageStu.getTotalPages());
		List<Student> stuList = pageStu.getContent();
		for (Student student : stuList) {
			System.out.println(student);
		}

	}

	@Test
	public void sortStuDoc() {
		String preTag = "<font color='red'>";
		String postTag = "</font>";
		Pageable pageable = PageRequest.of(0, 10);

		// 查询结果以money进行排序
		SortBuilder<?> sortBuilder = new FieldSortBuilder("money").order(SortOrder.DESC);
		SortBuilder<?> sortBuilder2 = new FieldSortBuilder("age").order(SortOrder.DESC);

		SearchQuery query = new NativeSearchQueryBuilder()
				.withQuery(QueryBuilders.matchQuery("description", "a teacher"))
				.withHighlightFields(new HighlightBuilder.Field("description").preTags(preTag).postTags(postTag))
				.withSort(sortBuilder).withSort(sortBuilder2).withPageable(pageable).build();
		AggregatedPage<Student> pageStu = esTemplate.queryForPage(query, Student.class, new SearchResultMapper() {
			@Override
			public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
				List<Student> stuHighlight = new ArrayList<>();
				SearchHits hits = searchResponse.getHits();
				for (SearchHit h : hits) {
					HighlightField highlightField = h.getHighlightFields().get("description");
					Integer stuId = (Integer) h.getSourceAsMap().get("stuId");
					String name = (String) h.getSourceAsMap().get("name");
					Integer age = (Integer) h.getSourceAsMap().get("age");
					String sign = (String) h.getSourceAsMap().get("sign");
					Object money = h.getSourceAsMap().get("money");
					// 高亮数据
					String description = highlightField.getFragments()[0].toString();
					Student studentHL = new Student();
					studentHL.setDescription(description);
					studentHL.setStuId(Long.parseLong(stuId.toString()));
					studentHL.setName(name);
					studentHL.setAge(age);
					studentHL.setSign(sign);
					studentHL.setMoney(Float.parseFloat(money.toString()));
					stuHighlight.add(studentHL);
				}
				if (!stuHighlight.isEmpty()) {
					return new AggregatedPageImpl<>((List<T>) stuHighlight);
				}
				return null;
			}

			@Override
			public <T> T mapSearchHit(SearchHit searchHit, Class<T> aClass) {
				return null;
			}
		});

		System.out.println("总分页数:" + pageStu.getTotalPages());
		List<Student> stuList = pageStu.getContent();
		for (Student student : stuList) {
			System.out.println(student);
		}
	}

	@Test
	public void searchScrollStuDoc() {
		Pageable pageable = PageRequest.of(1, 2);
		// 查询结果以money进行排序
		SortBuilder<?> sortBuilder = new FieldSortBuilder("money").order(SortOrder.DESC);
		SortBuilder<?> sortBuilder2 = new FieldSortBuilder("age").order(SortOrder.DESC);

		SearchQuery query = new NativeSearchQueryBuilder().withQuery(QueryBuilders.matchQuery("description", "中测"))
				.withSort(sortBuilder).withSort(sortBuilder2).withPageable(pageable).build();

		long scrollTimeout = 10000;
		ScrolledPage<Student> scrolledPage = esTemplate.startScroll(scrollTimeout, query, Student.class);

		System.out.println("总分页数:" + scrolledPage.getTotalPages());

		List<Student> resultList = new ArrayList<>();
		while (scrolledPage.hasContent()) {
			List<Student> list = scrolledPage.getContent();
			resultList.addAll(list);
			System.out.println(list);
			System.out.println("当前页的条数为：" + list.size() + "\n");
			scrolledPage = esTemplate.continueScroll(scrolledPage.getScrollId(), scrollTimeout, Student.class);
		}
	}
}