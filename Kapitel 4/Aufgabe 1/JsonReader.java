package de.bit.pl2.ex10;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class JsonReader {

    /**
     * Create list of Document objects from list of pmids
     *
     * @param listname
     * @return
     */
    public List<Document> readAllJsons(List<Integer> listname) {
        List<Document> list = new ArrayList<>();
        try {
            for (int i : listname) {
                URL url = prepareURL(i);
                JSONObject json = readJsonFromUrl(url);
                URL urlText = prepareURLAbstract(i);
                JSONObject jsonText = readJsonArrayFromUrl(urlText);

                Document doc = filterJsonForPubtype(json, jsonText, i);
                list.add(doc);

            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return list;
    }

    // https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esummary.fcgi?db=pubmed&id=25081398&retmode=json
    // ground truth. use this URL to get list of pubtypes
    public URL prepareURL(int pmid) throws MalformedURLException {
        String idString = String.valueOf(pmid);
        URL url = new URL("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esummary.fcgi?db=pubmed&" +
                "id=" + idString + "&retmode=json");
        return url;
    }

    //https://www.ncbi.nlm.nih.gov/CBBresearch/Lu/Demo/RESTful/tmTool.cgi/BioConcept/25081398/JSON/
    // use this to get abstract
    public URL prepareURLAbstract(int pmid) throws MalformedURLException {
        String idString = String.valueOf(pmid);
        URL url = new URL("https://www.ncbi.nlm.nih.gov/CBBresearch/Lu/Demo/RESTful/tmTool.cgi/BioConcept/" +
                idString + "/JSON/");
        return url;
    }


    /**
     * Create JSON object from provided URL
     *
     * @param url
     * @return json JSON object
     * @throws IOException
     */
    public JSONObject readJsonFromUrl(URL url) throws IOException {
        JSONObject json = new JSONObject(IOUtils.toString(url, Charset.forName("UTF-8")));
        return json;
    }

    /**
     * Create JSON object from provided URL, containing an array of JSONs
     *
     * @param url
     * @return json JSON object
     * @throws IOException
     */
    public JSONObject readJsonArrayFromUrl(URL url) throws IOException {
        JSONArray jArray = new JSONArray(IOUtils.toString(url, Charset.forName("UTF-8")));
        JSONObject json = jArray.getJSONObject(0);
        return json;
    }

    /**
     * Create Document object from the JSONs corresponding to that pmid
     *
     * @param json     JSON object containing most Document information
     * @param jsonText JSON object containing the abstract for the pmid
     * @param pmid     PubMedID of interest
     * @return doc Document object
     */
    public Document filterJsonForPubtype(JSONObject json, JSONObject jsonText, int pmid) {
        Document doc = new Document();

        doc.setPmid(pmid);

        JSONArray pmidJson = json.getJSONObject("result").getJSONObject(String.valueOf(pmid)).getJSONArray("pubtype");
        List<String> pubtype = new ArrayList<>();
        for (int j = 0; j < pmidJson.length(); ++j) {
            String name = pmidJson.getString(j);
            pubtype.add(name);
        }
        doc.setPubtype(pubtype);

        String title = json.getJSONObject("result").getJSONObject(String.valueOf(pmid)).getString("title");
        doc.setTitle(title);

        String text = jsonText.getString("text");
        doc.setText(text);

        return doc;
    }

}