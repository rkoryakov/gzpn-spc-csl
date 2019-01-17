package ru.gzpn.spc.csl.services.bl;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.gzpn.spc.csl.model.interfaces.IDocument;
import ru.gzpn.spc.csl.model.interfaces.IWorkSet;
import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.model.repositories.DocumentRepository;
import ru.gzpn.spc.csl.services.bl.interfaces.IDocumentService;
import ru.gzpn.spc.csl.ui.common.I18n;

@Service
@Transactional
public class DocumentService implements IDocumentService, I18n {

	@Autowired
	DocumentRepository documentRepository;
	
	@Autowired
	MessageSource message;
	
	public DocumentService() {
		LocaleContextHolder.getLocale();
	}
	
	@Override
	public List<IDocument> getDocuments(IWorkSet workset) {
		
		// TODO Auto-generated method stub
		return null;
	}
}

class DocumentFilter {

	private String commonTextFilter;
	private String codeFilter;
	private String nameFilter;
	// and some other field filters...

	private DocumentFilter() {
			
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

	public Predicate<IDocument> filter(List<ColumnSettings> shownColumns) {
		// only common filter is working now
		return p -> {
			boolean result = false;
			// logger.debug("[filter] shownColumns {}", shownColumns);
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

		switch (column.getEntityFieldName()) {
		case IDocument.FIELD_NAME:
			result = document.getName().toLowerCase().startsWith(commonTextFilter);
			break;
		case IDocument.FIELD_CODE:
			result = document.getCode().toLowerCase().startsWith(commonTextFilter);
			break;
		case IDocument.FIELD_TYPE:
			//result = document.getType().toLowerCase().startsWith(commonTextFilter);
			break;
		
		default:
		}

		return result;
	}
}