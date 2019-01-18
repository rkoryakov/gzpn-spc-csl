package ru.gzpn.spc.csl.services.bl;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.gzpn.spc.csl.model.enums.DocType;
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
	
	@Override
	public Map<DocType, String> getDocumentTypeCaptions() {
		EnumMap<DocType, String> documentTypeCaptions = new EnumMap<>(DocType.class);
		for (DocType dt : DocType.values()) {
			documentTypeCaptions.put(dt, getI18nText(dt.getI18n(), message));
		}
		return documentTypeCaptions;
	}
	
	@Override
	public List<IDocument> getDocuments(IWorkSet workset) {
		return workset.getDocuments();
	}
	
	public static final class DocumentFilter {
		private String commonTextFilter;
		private String codeFilter;
		private String nameFilter;
		
		private Map<DocType, String> documentTypeCaptions;

		public DocumentFilter(Map<DocType, String> map) {
			this.documentTypeCaptions = map;
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
				documentTypeCaptions.get(document.getType()).toLowerCase().startsWith(commonTextFilter);
				break;
			default:
			}

			return result;
		}
	}
}


