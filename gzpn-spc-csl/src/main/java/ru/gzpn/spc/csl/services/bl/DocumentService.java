package ru.gzpn.spc.csl.services.bl;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vaadin.data.provider.QuerySortOrder;
import com.vaadin.shared.data.sort.SortDirection;

import ru.gzpn.spc.csl.model.Document;
import ru.gzpn.spc.csl.model.interfaces.IDocument;
import ru.gzpn.spc.csl.model.interfaces.IWorkSet;
import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.model.presenters.interfaces.IDocumentPresenter;
import ru.gzpn.spc.csl.model.repositories.DocumentRepository;
import ru.gzpn.spc.csl.services.bl.interfaces.IDocumentService;
import ru.gzpn.spc.csl.ui.common.IGridFilter;

@Service
@Transactional
public class DocumentService implements IDocumentService {

	@Autowired
	DocumentRepository documentRepository;
	
	@Autowired
	MessageSource source;
	
	@Override
	public List<IDocument> getDocuments(IWorkSet workset) {
		return documentRepository.findDocumentsByWorkset(workset);
	}
	
	@Override
	public long getDocumentsCount(IWorkSet workset) {
		return documentRepository.getCountByWorkId(workset.getId());
	}
	
	@Override
	public MessageSource getMessageSource() {
		return source;
	}
	
	
	@Override
	public Comparator<IDocumentPresenter> getSortComparator(List<QuerySortOrder> list) {

		return (a, b) -> {
			int result = 0;
			for (QuerySortOrder qso : list) {
				switch (qso.getSorted()) {		
				case IDocument.FIELD_NAME:
					result = a.getName().compareTo(b.getName());
					break;
				case IDocument.FIELD_CODE:
					result = a.getCode().compareTo(b.getCode());
					break;
				case IDocument.FIELD_TYPE:
					result = a.getType().compareTo(b.getType());
					break;
				case IDocument.FIELD_WORK:
					result = a.getWorkText().compareTo(b.getWorkText());
					break;
				case IDocument.FIELD_WORKSET:
					result = a.getWorksetText().compareTo(b.getWorksetText());
					break;
				case IDocument.FIELD_ID:
					result = a.getId().compareTo(b.getId());
					break;
				case IDocument.FIELD_VERSION:
					result = a.getVersion().compareTo(b.getVersion());
					break;
				case IDocument.FIELD_CREATE_DATE:
					result = a.getCreateDate().compareTo(b.getCreateDate());
					break;
				case IDocument.FIELD_CHANGE_DATE:
					result = a.getChangeDate().compareTo(b.getChangeDate());
					break;
					default:
				}
				if (qso.getDirection() == SortDirection.DESCENDING) {
					result *= -1;
				}
			}
			return result;
		};
	}
	
	public static final class DocumentFilter implements IGridFilter<IDocument> {
		private String commonTextFilter;
		private String codeFilter;
		private String nameFilter;
		private MessageSource source;
		
		public DocumentFilter(MessageSource source) {
			this.source = source;
		}

		public String getCommonTextFilter() {
			return commonTextFilter;
		}

		public void setCommonTextFilter(String commonTextFilter) {
			this.commonTextFilter = commonTextFilter.toLowerCase();
		}

		public String getCodeFilter() {
			return codeFilter;
		}

		public void setCodeFilter(String codeFilter) {
			this.codeFilter = codeFilter;
		}

		public String getNameFilter() {
			return nameFilter;
		}

		public void setNameFilter(String nameFilter) {
			this.nameFilter = nameFilter;
		}

		@Override
		public Predicate<IDocument> getFilterPredicate(List<ColumnSettings> shownColumns) {
			// only common filter is working now
			return p -> {
				boolean result = false;
				if (StringUtils.isNoneBlank(commonTextFilter) && Objects.nonNull(shownColumns)) {
					for (ColumnSettings column : shownColumns) {
						if (applyColumnFilter(p, column)) {
							result = true;
							break;
						}
					}
				} else {
					result = true;
				}
				return result;
			};
		}

		private boolean applyColumnFilter(IDocument document, ColumnSettings column) {
			boolean result = false;

			if (document instanceof IDocumentPresenter) {
				IDocumentPresenter documentPresenter = (IDocumentPresenter)document;
				switch (column.getEntityFieldName()) {
				case IDocument.FIELD_NAME:
					result = documentPresenter.getName().toLowerCase().startsWith(commonTextFilter);
					break;
				case IDocument.FIELD_CODE:
					result = documentPresenter.getCode().toLowerCase().startsWith(commonTextFilter);
					break;
				case IDocument.FIELD_TYPE:
					result = documentPresenter.getType().getText().toLowerCase().startsWith(commonTextFilter);
					break;
				case IDocument.FIELD_WORK:
					result = documentPresenter.getWorkText().toLowerCase().startsWith(commonTextFilter);
					break;
				case IDocument.FIELD_WORKSET:
					result = documentPresenter.getWorksetText().toLowerCase().startsWith(commonTextFilter);
					break;
				default:
				}
			}

			return result;
		}
	}

	@Override
	public void save(IDocument bean) {
		
		if (bean.getId() != null) {
			Optional<Document> doc = this.documentRepository.findById(bean.getId());
			if (doc.isPresent()) {
				IDocument document = doc.get();
				document.setCode(bean.getCode());
				document.setName(bean.getName());
				document.setType(bean.getType());
			
				this.documentRepository.save((Document)document);
			}
		} else {
			this.documentRepository.save((Document)bean);
		}
	}
	
	@Override
	public void remove(IDocument bean) {
		if (bean.getId() != null) {
			this.documentRepository.deleteById(bean.getId());
		}
	}
}
