from google.cloud import storage
import logging
import base64

storage_client = storage.Client()
STORAGE_BUCKET = 'andy-data-store'

def write_issue_to_storage(request):
    request_json = request.get_json()
    
    received_message = base64.b64decode(request_json['message']['data']).decode('utf-8')
    
    bucket = storage_client.get_bucket(STORAGE_BUCKET)

    blob = bucket.blob('Tracked_issues/issues.txt')

    if blob.exists():
        blob.download_to_filename('/tmp/issues.txt')

    with open ('tmp/issues.txt', 'a') as issue_tracker:
        issue_tracker.write('\n' + received_message)
        issue_tracker.close()

    blob.upload_from_filename('/tmp/issues.txt')

    return f'<html><head><meta name="google-site-verification" content="ISRzXQVIMsrx6MCd93RWp4oQZGFTpNSkA5AGprnwto4" /></head><body></body></html>'
